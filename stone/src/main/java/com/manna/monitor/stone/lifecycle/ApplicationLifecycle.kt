package com.manna.monitor.stone.lifecycle

import android.app.Application
import android.content.Context

/**
 * Define lifecycle methods what you want to use
 */
interface ApplicationLifecycle {

    /** Lifecycle onAttachBaseContext */
    fun onAttachBaseContext(context: Context)

    /** Lifecycle onCreate */
    fun onCreate(application: Application)

    /** marking priority,0 is highest priority,next is 1,2,3...100 ,you can custom it */
    fun priority(): Int
}