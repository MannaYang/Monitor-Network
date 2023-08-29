package com.manna.monitor.http

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Use CoroutineScope(Dispatchers.IO) to dispatch http interceptor
 */
object DispatchProvider {

    //http data
    private val dataList: ArrayList<HttpResult> by lazy { arrayListOf() }

    //http interceptor
    //private val dispatchListener: ArrayList<HttpInterceptorListener> by lazy { arrayListOf() }

    //http interceptor
    private val loaderProxy by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { InterceptorServiceLoader() }

    //coroutine marker
    private var isCancel = true
    private var isStart = false

    //coroutine
    private var dispatchJob: Job? = null

    fun addHttpResult(data: HttpResult) {
        Log.d("Monitor", "Cache HttpResult...")
        dataList.add(data)
        if (isCancel && dispatchJob?.isActive != true) {
            isCancel = false
            dispatch()
        }
    }

    private fun dispatch() {
        isCancel = false
        dispatchJob = CoroutineScope(Dispatchers.IO).launch {
            if (isStart) {
                Log.d("Monitor", "If coroutine has already started,cancel it")
                this.cancel()
            }
            isStart = true
            Log.d("Monitor", "Coroutine is starting...")
            isCancel = false
            while (dataList.isNotEmpty()) {
                kotlin.runCatching { dispatchResult() }
            }
            Log.d("Monitor", "Coroutine is finished...")
            isCancel = true
            isStart = false
            this.cancel()
        }
    }

    @Synchronized
    private fun dispatchResult() {
        Log.d(
            "Monitor",
            "Start to dispatch result, you can implement [HttpInterceptorListener] to intercept result"
        )
        val result = dataList[0]
        if (dataList.isNotEmpty()) dataList.removeAt(0)
        loaderProxy.loader.forEach { listener ->
            listener.onIntercept(HttpResultParser.parseResult(result))
        }
    }
}