package com.pandulapeter.beagle.logKtor

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.Url
import io.ktor.http.charset
import io.ktor.http.content.OutgoingContent
import io.ktor.http.fullPath
import io.ktor.util.AttributeKey
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.close
import io.ktor.utils.io.core.readText
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.writeFully
import io.ktor.utils.io.writer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Modified version of https://ktor.io/clients/http-client/features/logging.html
 */
internal class KtorLogger {

    private suspend fun logRequest(request: HttpRequestBuilder): OutgoingContent? {
        val content = request.body as OutgoingContent
        val channel = ByteChannel()
        val result = content.observe(channel)
        val text = readPayload(content.contentType, channel)
        BeagleKtorLogger.logNetworkEvent(
            isOutgoing = true,
            url = "[${request.method.value}] ${Url(request.url)}",
            payload = text,
            headers = readHeaders(request.headers.entries(), content.headers)
        )
        return result
    }

    private fun logResponse(response: HttpResponse) {
        BeagleKtorLogger.logNetworkEvent(
            isOutgoing = false,
            url = "[${response.call.request.method.value}] ${response.status.value} ${response.call.request.url.fullPath}",
            payload = "Response payload logging not implemented yet",//TODO: readPayload(response.contentType(), response.content),
            headers = readHeaders(response.headers.entries()),
            duration = response.responseTime.timestamp - response.requestTime.timestamp
        )
    }

    private fun logException(context: HttpRequestBuilder, cause: Throwable) {
        BeagleKtorLogger.logNetworkEvent(
            isOutgoing = false,
            url = "[${context.method.value}] FAIL ${Url(context.url)}",
            payload = cause.message ?: "HTTP Failed"
        )
    }

    private fun readHeaders(
        requestHeaders: Set<Map.Entry<String, List<String>>>,
        contentHeaders: Headers? = null
    ): List<String> = mutableListOf<String>().apply {
        requestHeaders.forEach { (key, values) ->
            add("[$key] ${values.joinToString("; ")}")
        }
        contentHeaders?.forEach { key, values ->
            add("[$key] ${values.joinToString("; ")}")
        }
    }

    private suspend fun readPayload(contentType: ContentType?, content: ByteReadChannel) = (try {
        content.readText(contentType?.charset() ?: Charsets.UTF_8)
    } catch (_: Exception) {
        "Cannot parse payload"
    }).let { if (it.isBlank()) "No payload" else it }


    private suspend fun OutgoingContent.observe(log: ByteWriteChannel): OutgoingContent = when (this) {
        is OutgoingContent.ByteArrayContent -> {
            log.writeFully(bytes())
            log.close()
            this
        }
        is OutgoingContent.ReadChannelContent -> {
            val responseChannel = ByteChannel()
            val content = readFrom()
            content.copyToBoth(log, responseChannel)
            LoggingContent(responseChannel)
        }
        is OutgoingContent.WriteChannelContent -> {
            val responseChannel = ByteChannel()
            val content = toReadChannel()
            content.copyToBoth(log, responseChannel)
            LoggingContent(responseChannel)
        }
        else -> {
            log.close()
            this
        }
    }

    private fun OutgoingContent.WriteChannelContent.toReadChannel(): ByteReadChannel = GlobalScope.writer(Dispatchers.Unconfined) {
        writeTo(channel)
    }.channel

    private suspend inline fun ByteReadChannel.readText(charset: Charset): String = readRemaining().readText(charset = charset)

    private fun ByteReadChannel.copyToBoth(first: ByteWriteChannel, second: ByteWriteChannel) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            try {
                while (!isClosedForRead && (!first.isClosedForWrite || !second.isClosedForWrite)) {
                    readRemaining(CHUNK_BUFFER_SIZE).use {
                        try {
                            first.writePacket(it.copy())
                            second.writePacket(it.copy())
                        } catch (cause: Throwable) {
                            this@copyToBoth.cancel(cause)
                            first.close(cause)
                            second.close(cause)
                        }
                    }
                }
            } catch (cause: Throwable) {
                first.close(cause)
                second.close(cause)
            } finally {
                first.close()
                second.close()
            }
        }
    }

    internal class LoggingContent(private val channel: ByteReadChannel) : OutgoingContent.ReadChannelContent() {
        override fun readFrom(): ByteReadChannel = channel
    }

    companion object : HttpClientFeature<Nothing, KtorLogger> {
        private const val CHUNK_BUFFER_SIZE = 4096L

        override val key: AttributeKey<KtorLogger> = AttributeKey("BeagleClientLogging")

        override fun prepare(block: Nothing.() -> Unit) = KtorLogger()

        override fun install(feature: KtorLogger, scope: HttpClient) {
            scope.sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                val response = try {
                    feature.logRequest(context)
                } catch (_: Throwable) {
                    null
                } ?: subject

                try {
                    proceedWith(response)
                } catch (cause: Throwable) {
                    feature.logException(context, cause)
                    throw cause
                }
            }
            scope.responsePipeline.intercept(HttpResponsePipeline.Receive) {
                feature.logResponse(context.response)
                proceedWith(subject)
            }
        }
    }
}