package com.manna.monitor.http

import android.util.Log
import com.manna.monitor.http.export.HttpDataResult
import com.manna.monitor.stone.http.GsonProvider
import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException


/**
 * Parses HttpResult and assembles HttpDataResult for external use
 */
internal object HttpResultParser {

    fun parseResult(bean: HttpResult): HttpDataResult {
        Log.d("Monitor", "Starting sealed HttpResult...")
        val chain = bean.chain
        val httpCode = bean.httpCode
        val httpMessage = bean.httpMessage
        val startTime = bean.startTime
        val endTime = bean.endTime
        Log.d("Monitor", "Parsing params...")
        val request = chain.request()
        val url = request.url.toString()
        Log.d("Monitor", "Parsing headers...")
        val headers = parseRequestHeader(request)
        Log.d("Monitor", "Parsing request body...")
        val body = parseRequestBody(request)
        Log.d("Monitor", "Parsing response data...")
        val data = bean.responseData
        var code = ""
        var success = false
        kotlin.runCatching {
            //According to the interface returns the actual json format parsing
            val jsonObject = JSONObject(data ?: "")
            code = jsonObject.getString("code")
            success = jsonObject.getBoolean("success")
        }.onFailure {
            //if parse error,return the default value
        }
        val dataStr = if (httpCode == 500 || httpCode == 404) " $httpCode : $httpMessage" else data
        Log.d("Monitor", "Finished sealed HttpResult...")
        return HttpDataResult(
            httpCode, success, code, url, headers, body, dataStr, startTime, endTime
        )
    }

    /**
     * parse request headers
     */
    private fun parseRequestHeader(request: Request): String {
        val headers = mutableMapOf<Any?, Any?>()
        request.headers.forEach {
            headers[it.first] = it.second
        }
        return GsonProvider.gson.toJson(headers)
    }

    /**
     * parse request body
     */
    private fun parseRequestBody(request: Request): String {
        return try {
            val body = request.body
            val bodyStr = StringBuilder("")
            when {
                request.method == "POST" -> {
                    bodyStr.append(parsePostBody(request))
                }

                body is FormBody -> {
                    bodyStr.append(parseFormBody(body))
                }

                body is MultipartBody -> {
                    //file not enable parse
                }

                else -> {
                    //put or get
                    bodyStr.append(request.url.encodedQuery)
                }
            }
            bodyStr.toString()
        } catch (_: Exception) {
            ""
        }
    }


    /**
     * parse form body
     */
    private fun parseFormBody(body: FormBody): String {
        val paramsMap = HashMap<String, String>()
        for (i in 0 until body.size) {
            paramsMap[body.encodedName(i)] = body.encodedValue(i)
        }
        return body.toString()
    }

    /**
     * parse post body
     */
    private fun parsePostBody(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = okio.Buffer()
            copy.body!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            ""
        }
    }
}