package com.manna.monitor.http

/**
 * Interceptor config
 */
object HttpConfig {

    /**
     * You can use the web admin backend to download the rules,
     * here abbreviated to define the default whitelist array
     */
    val whiteHost = arrayListOf("oapi.dingtalk.com", "api.dingtalk.com")
}