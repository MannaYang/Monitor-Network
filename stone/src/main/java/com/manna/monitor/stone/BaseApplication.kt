package com.manna.monitor.stone

import android.app.Application
import android.content.Context
import com.manna.monitor.stone.lifecycle.ServiceLoaderProxy

/**
 * Global Application
 */
open class BaseApplication : Application() {

    private val loaderProxy by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ServiceLoaderProxy() }

    companion object {
        private lateinit var application: Application

        fun getInstance(): Context = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        loaderProxy.lifecycleQueue.forEach { it.onCreate(this) }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        loaderProxy.lifecycleQueue.forEach { it.onAttachBaseContext(this) }
    }
}