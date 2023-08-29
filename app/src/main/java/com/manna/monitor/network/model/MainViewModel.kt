package com.manna.monitor.network.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manna.monitor.network.repository.MainRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("CoroutineException", throwable.toString())
    }

    fun testPost() {
        launch({
            val result = MainRepository.instance.testPost()
            Log.d("PostRequest", result.toString())
        })
    }

    private fun launch(
        block: suspend CoroutineScope.() -> Unit,
        onComplete: suspend CoroutineScope.() -> Unit = {},
        onError: (e: Exception) -> Unit = { _ -> }
    ) {
        viewModelScope.launch(exceptionHandler) {
            try {
                block()
            } catch (e: Exception) {
                onError(e)
            } finally {
                onComplete()
            }
        }
    }
}