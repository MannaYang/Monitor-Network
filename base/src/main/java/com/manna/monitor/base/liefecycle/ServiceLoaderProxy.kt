package com.manna.monitor.base.liefecycle

import java.util.*

/**
 * with google auto-service,it will collect all @AutoService annotation class
 */
class ServiceLoaderProxy {

    private val loader: ServiceLoader<ApplicationLifecycle> by lazy {
        ServiceLoader.load(ApplicationLifecycle::class.java)
    }

    val lifecycleQueue by lazy { loader.sortedWith(compareBy { it.priority() }) }
}