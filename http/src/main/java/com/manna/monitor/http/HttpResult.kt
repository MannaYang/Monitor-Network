package com.manna.monitor.http

import okhttp3.Interceptor

/**
 * Define okhttp interceptor result class,it is metadata
 */
data class HttpResult(
    val startTime: Long,
    val endTime: Long,
    val chain: Interceptor.Chain,
    val httpCode: Int,
    val httpMessage: String,
    val responseData: String?
)
