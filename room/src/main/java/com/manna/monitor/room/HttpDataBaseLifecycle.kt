package com.manna.monitor.room

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.auto.service.AutoService
import com.manna.monitor.room.db.HttpDataBase
import com.manna.monitor.stone.lifecycle.ApplicationLifecycle

/**
 * add google [@AutoService] annotation,to implementation it
 * Init Room.databaseBuilder
 */
@AutoService(ApplicationLifecycle::class)
class HttpDataBaseLifecycle : ApplicationLifecycle {

    override fun onAttachBaseContext(context: Context) {
        //do somethings
    }

    override fun onCreate(application: Application) {
        Log.d("Monitor", "Init room database and dao impl")
        val dataBase =
            Room.databaseBuilder(application, HttpDataBase::class.java, "monitor_network.db")
                .build()
        HttpDataDaoProvider.setHttpDataDao(dataBase.httpDataDao())
    }

    override fun priority(): Int {
        //see also [ApplicationLifecycleProxy]
        return 4
    }
}