package com.manna.monitor.http

import com.manna.monitor.http.export.HttpInterceptorListener
import java.util.ServiceLoader

/**
 * To collect HttpInterceptorListener instance
 */
class InterceptorServiceLoader {
    val loader: ServiceLoader<HttpInterceptorListener> by lazy {
        ServiceLoader.load(HttpInterceptorListener::class.java)
    }
}
