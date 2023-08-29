package com.manna.monitor.stone.http

import okhttp3.Interceptor

/**
 * custom okhttp3.Interceptor
 *
 * define a interface,other module will use @AutoService annotation to get it instance
 */
interface NetworkInterceptor : Interceptor