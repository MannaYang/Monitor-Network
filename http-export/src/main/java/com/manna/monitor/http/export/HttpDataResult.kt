package com.manna.monitor.http.export

/**
 * Define HttpDataResult,you can use this class to store data
 */
data class HttpDataResult(
    //http request code
    val httpCode: Int,
    //data response success
    val success: Boolean,
    //data response Code
    val code: String? = "",
    //data request url
    val url: String,
    //data request header
    val headers: String,
    //data request body
    val body: String?,
    //data response data
    val data: String?,
    //data request time
    val startTime: Long, val endTime: Long
)
