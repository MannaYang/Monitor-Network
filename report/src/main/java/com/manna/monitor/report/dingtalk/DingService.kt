package com.manna.monitor.report.dingtalk

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * http interface
 * @see [https://oapi.dingtalk.com/]
 * @see [https://api.dingtalk.com/]
 */
interface DingService {

    @POST("robot/send")
    suspend fun report(
        @Query("access_token") token: String, @Body params: MutableMap<String, Any>
    ): JsonObject?

    @POST("topapi/message/corpconversation/asyncsend_v2")
    suspend fun asyncReport(
        @Query("access_token") token: String, @Body params: MutableMap<String, Any>
    ): JsonObject?

    @POST("v1.0/oauth2/accessToken")
    suspend fun getAccessToken(@Body params: MutableMap<String, Any>): JsonObject?

    @GET("user/get_by_mobile")
    suspend fun getUseId(
        @Query("access_token") token: String, @Query("mobile") mobile: String
    ): JsonObject?
}