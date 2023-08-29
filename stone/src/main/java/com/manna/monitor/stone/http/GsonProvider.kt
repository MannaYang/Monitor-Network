package com.manna.monitor.stone.http

import com.google.gson.Gson

object GsonProvider {
    val gson by lazy { Gson() }
}