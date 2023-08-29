package com.manna.monitor.stone.lifecycle

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.manna.monitor.stone.data.DataStoreProvider

/**
 * Add google [@AutoService] annotation,to implementation it
 */
@AutoService(ApplicationLifecycle::class)
class ApplicationLifecycleProxy : ApplicationLifecycle {

    override fun onAttachBaseContext(context: Context) {
        //do somethings
    }

    override fun onCreate(application: Application) {
        //do somethings
        DataStoreProvider.initDataStore(application)
    }

    override fun priority(): Int {
        //in base application context,do somethings highest priority,so you should return 0
        return 0
    }
}