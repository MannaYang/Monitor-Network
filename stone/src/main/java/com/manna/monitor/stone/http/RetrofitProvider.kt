package com.manna.monitor.stone.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ServiceLoader
import java.util.concurrent.TimeUnit

/**
 * Define Retrofit external operation
 */
class RetrofitProvider private constructor() {

    companion object {
        val instance = RetrofitProvider()
    }

    private val interceptorLoader: ServiceLoader<NetworkInterceptor> by lazy {
        ServiceLoader.load(NetworkInterceptor::class.java)
    }
    private val logging by lazy {
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    private val client by lazy {
        OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).addInterceptor(logging).apply {
                interceptorLoader.forEach { this.addInterceptor(it) }
            }.build()
    }

    private val apiServiceMap by lazy { mutableMapOf<Any, Any>() }
    private val retrofitMap by lazy { mutableMapOf<Any, Retrofit>() }

    @Suppress("UNCHECKED_CAST")
    fun <T> getApiService(baseUrl: String, apiService: Class<T>, isCreate: Boolean = false): T {
        val cacheApiService = apiServiceMap[apiService.name]
        return if (cacheApiService == null || isCreate) {
            val service: T = getRetrofit(baseUrl).create(apiService)
            apiServiceMap[apiService.name] = service!!
            service
        } else cacheApiService as T
    }

    private fun getRetrofit(baseUrl: String): Retrofit {
        val cacheRetrofit = retrofitMap[baseUrl]
        return if (cacheRetrofit == null) {
            val retrofit = Retrofit.Builder().baseUrl(baseUrl).client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.gson)).build()
            retrofitMap[baseUrl] = retrofit
            retrofit
        } else cacheRetrofit
    }
}