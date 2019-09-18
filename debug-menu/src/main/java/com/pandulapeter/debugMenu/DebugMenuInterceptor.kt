package com.pandulapeter.debugMenu

import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenuCore.DebugMenuInterceptor
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

object DebugMenuInterceptor : DebugMenuInterceptor {

    @Volatile
    private var headersToRedact = emptySet<String>()
    private val UTF8 = Charset.forName("UTF-8")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val logBody = true
        val logHeaders = true

        val requestBody = request.body
        val hasRequestBody = requestBody != null

        val connection = chain.connection()


        var requestStartMessage = ("--> "
                + request.method
                + ' '.toString() + request.url
                + if (connection != null) " " + connection.protocol() else "")
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody!!.contentLength() + "-byte body)"
        }
        //debugMenu.log(requestStartMessage)

        DebugMenu.logNetworkEvent(
            NetworkEvent(
                isOutgoing = true,
                url = "[${request.method}] ${request.url}"
            )
        )


        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
//                    debugMenu.log("Content-Type: " + requestBody.contentType()!!)
                }
                if (requestBody.contentLength() != -1L) {
//                    debugMenu.log("Content-Length: " + requestBody.contentLength())
                }
            }

            val headers = request.headers
            var i = 0
            val count = headers.size
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                    logHeader(headers, i)
                }
                i++
            }

            if (!logBody || !hasRequestBody) {
//                debugMenu.log("--> END " + request.method)
            } else if (bodyHasUnknownEncoding(request.headers)) {
//                debugMenu.log("--> END " + request.method + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)

                var charset: Charset? = UTF8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                //debugMenu.log("")
                if (isPlaintext(buffer)) {
//                    debugMenu.log(buffer.readString(charset!!))
//                    debugMenu.log(
//                        "--> END " + request.method
//                                + " (" + requestBody.contentLength() + "-byte body)"
//                    )
                } else {
//                    debugMenu.log(
//                        ("--> END " + request.method + " (binary "
//                                + requestBody.contentLength() + "-byte body omitted)")
//                    )
                }
            }
        }

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
//            debugMenu.log("<-- HTTP FAILED: $e")
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body
        val contentLength = responseBody!!.contentLength()
//        debugMenu.log(
//            ("<-- "
//                    + response.code
//                    + (if (response.message.isEmpty()) "" else ' ' + response.message)
//                    + ' '.toString() + response.request.url
//                    + " (" + tookMs + "ms)")
//        )


        DebugMenu.logNetworkEvent(
            NetworkEvent(
                isOutgoing = false,
                url = "${response.code} [${request.method}] ${request.url}"
            )
        )

        if (logHeaders) {
            val headers = response.headers
            var i = 0
            val count = headers.size
            while (i < count) {
                logHeader(headers, i)
                i++
            }

            if (!logBody || !response.promisesBody()) {
//                debugMenu.log("<-- END HTTP")
            } else if (bodyHasUnknownEncoding(response.headers)) {
//                debugMenu.log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer

                var gzippedLength: Long? = null
                if ("gzip".equals(headers["Content-Encoding"] ?: "", ignoreCase = true)) {
                    gzippedLength = buffer.size
                    var gzippedResponseBody: GzipSource? = null
                    try {
                        gzippedResponseBody = GzipSource(buffer.clone())
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    } finally {
                        gzippedResponseBody?.close()
                    }
                }

                var charset: Charset? = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                if (!isPlaintext(buffer)) {
//                    debugMenu.log("")
//                    debugMenu.log("<-- END HTTP (binary " + buffer.size + "-byte body omitted)")
                    return response
                }

                if (contentLength != 0L) {
//                    debugMenu.log("")
//                    debugMenu.log(buffer.clone().readString(charset!!))
                }

                if (gzippedLength != null) {
//                    debugMenu.log(
//                        ("<-- END HTTP (" + buffer.size + "-byte, "
//                                + gzippedLength + "-gzipped-byte body)")
//                    )
                } else {
//                    debugMenu.log("<-- END HTTP (" + buffer.size + "-byte body)")
                }
            }
        }
        return response
    }


    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }

    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return (contentEncoding != null
                && !contentEncoding.equals("identity", ignoreCase = true)
                && !contentEncoding.equals("gzip", ignoreCase = true))
    }

    private fun logHeader(headers: Headers, i: Int) {
        val value = if (headersToRedact.contains(headers.name(i))) "██" else headers.value(i)
//        debugMenu.log(headers.name(i) + ": " + value)
    }
}