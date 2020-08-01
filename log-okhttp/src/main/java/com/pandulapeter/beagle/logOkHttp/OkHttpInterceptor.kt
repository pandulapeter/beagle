package com.pandulapeter.beagle.logOkHttp

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.EOFException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * Modified version of https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
 */
internal class OkHttpInterceptor : Interceptor {

    private val utf8 = Charset.forName("UTF-8")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body
        val requestJson = if (requestBody == null) {
            "No payload"
        } else if (bodyHasUnknownEncoding(request.headers)) {
            "Encoded payload"
        } else if (requestBody.contentLength() > MAX_STRING_LENGTH) {
            "Payload too large"
        } else {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset: Charset? = utf8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(utf8)
            }
            if (isPlaintext(buffer)) {
                charset?.let { buffer.readString(it) } ?: ""
            } else {
                "Binary payload, ${requestBody.contentLength()}-byte body"
            }
        }
        BeagleOkHttpLogger.logNetworkEvent(
            isOutgoing = true,
            url = "[${request.method}] ${request.url}",
            payload = requestJson,
            headers = request.headers.map { "[${it.first}] ${it.second}" }
        )
        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (exception: Exception) {
            BeagleOkHttpLogger.logNetworkEvent(
                isOutgoing = false,
                url = "[${request.method}] FAIL ${request.url}",
                payload = exception.message ?: "HTTP Failed"
            )
            throw exception
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body
        val contentType = responseBody?.contentType()
        val responseJson = if ((contentType == null || contentType.subtype == "json") && responseBody?.source()?.buffer?.isProbablyUtf8() == true) response.body?.string() else null
        BeagleOkHttpLogger.logNetworkEvent(
            isOutgoing = false,
            url = "[${request.method}] ${response.code} ${request.url}",
            payload = responseJson ?: response.message,
            headers = response.headers.map { "[${it.first}] ${it.second}" },
            duration = tookMs
        )
        return response.newBuilder().body(responseJson?.toResponseBody(responseBody?.contentType()) ?: responseBody).build()
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
            return false
        }
    }

    private fun bodyHasUnknownEncoding(headers: Headers) = headers["Content-Encoding"].let { contentEncoding ->
        (contentEncoding != null
                && !contentEncoding.equals("identity", ignoreCase = true)
                && !contentEncoding.equals("gzip", ignoreCase = true))
    }

    private fun Buffer.isProbablyUtf8(): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = size.coerceAtMost(64)
            copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: EOFException) {
            return false
        }
    }

    companion object {
        private const val MAX_STRING_LENGTH = 4096L
    }
}