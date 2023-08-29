package com.manna.monitor.network.repository

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * http interface
 * @see [https://jsonplaceholder.typicode.com/]
 */
interface MainService {

    @POST("posts")
    suspend fun testPost(@Body params: MutableMap<String, Any>): Any?
}