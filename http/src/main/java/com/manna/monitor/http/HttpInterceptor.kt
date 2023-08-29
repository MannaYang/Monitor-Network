package com.manna.monitor.http

import android.util.Log
import com.google.auto.service.AutoService
import com.manna.monitor.stone.http.NetworkInterceptor
import okhttp3.*
import okio.Buffer
import okio.BufferedSource
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.charset.UnsupportedCharsetException
import java.util.Arrays


/**
 * [com.manna.monitor.stone.http.NetworkInterceptor]
 */
@AutoService(NetworkInterceptor::class)
open class HttpInterceptor : NetworkInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("Monitor", "Okhttp request interceptor is starting...")
        val startTime = System.currentTimeMillis()
        val request = chain.request()
        val response = chain.proceed(request)
        val endTime = System.currentTimeMillis()
        if (request.url.host in HttpConfig.whiteHost) {
            Log.d(
                "Monitor",
                "Okhttp request url is in white list,\nrequest url is: ${request.url},\nwhite list is: ${HttpConfig.whiteHost}"
            )
            return response
        }
        Log.d("Monitor", "Okhttp request interceptor is finished...")
        DispatchProvider.addHttpResult(
            HttpResult(
                startTime,
                endTime,
                chain,
                response.code,
                response.message,
                parseResponseBody(chain, response.body?.source())
            )
        )
        return response
    }

    /**
     * parse response body
     */
    private fun parseResponseBody(chain: Interceptor.Chain, source: BufferedSource?): String? {
        Log.d("Monitor", "Starting parse response body...")
        val startTime = System.currentTimeMillis()
        if (source == null) return ""
        try {
            source.request(Long.MAX_VALUE)
            val buffer: Buffer = source.buffer
            var charset: Charset? = StandardCharsets.UTF_8
            val contentType = chain.request().body?.contentType()
            if (contentType != null) {
                charset = contentType.charset(StandardCharsets.UTF_8)
            }
            if (chain.request().body?.contentLength() != 0L) {
                Log.d(
                    "Monitor",
                    "Finished parse response body,cost time: " + (System.currentTimeMillis() - startTime).toString()
                )
                return buffer.clone().readString(charset!!)
            }
        } catch (e: UnsupportedCharsetException) {
            Log.d(
                "Monitor",
                "Failed to parse response body,error is: " + Arrays.toString(e.stackTrace)
            )
        }
        return null
    }
}