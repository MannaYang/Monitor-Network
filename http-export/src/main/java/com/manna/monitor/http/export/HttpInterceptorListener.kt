package com.manna.monitor.http.export

/**
 * Dispatch HttpDataResult
 */
interface HttpInterceptorListener {
    fun onIntercept(httpData: HttpDataResult)
}