package com.manna.monitor.network.repository

import com.manna.monitor.stone.http.RetrofitProvider

class MainRepository private constructor() {

    companion object {
        val instance = MainRepository()
    }

    private val mainApiService by lazy {
        RetrofitProvider.instance.getApiService(
            "https://jsonplaceholder.typicode.com/", MainService::class.java
        )
    }

    /**
     * Test RESTful API
     */
    suspend fun testPost(): Any? {
        val params = mutableMapOf(
            "code" to 0,
            "success" to true,
            "result" to mutableMapOf<String, Any>(
                "content" to "This is test RESTful API",
                "completed" to true
            )
        )
        return mainApiService.testPost(params)
    }
}