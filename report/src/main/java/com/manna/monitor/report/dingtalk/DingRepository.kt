package com.manna.monitor.report.dingtalk

import android.annotation.SuppressLint
import android.content.res.Resources
import com.google.gson.JsonObject
import com.manna.monitor.stone.BaseApplication
import com.manna.monitor.stone.http.RetrofitProvider

class DingRepository private constructor() {

    companion object {
        val instance = DingRepository()
    }

    private val userApiService by lazy {
        RetrofitProvider.instance.getApiService(
            "https://api.dingtalk.com/", DingService::class.java
        )
    }

    private val reportApiService by lazy {
        RetrofitProvider.instance.getApiService(
            "https://oapi.dingtalk.com/", DingService::class.java, isCreate = true
        )
    }

    /**
     * report robot
     */
    suspend fun report(token: String, params: MutableMap<String, Any>): JsonObject? {
        return reportApiService.report(token, params)
    }

    /**
     * async report workspace
     */
    suspend fun asyncReport(token: String, title: String, content: String): JsonObject? {
        val markdown = mutableMapOf("title" to title, "text" to content)
        val params = mutableMapOf(
            "agent_id" to getAgentId(),
            "userid_list" to getUsersId(),
            "msg" to mutableMapOf("msgtype" to "markdown", "markdown" to markdown)
        )
        return reportApiService.asyncReport(token, params)
    }

    /**
     * ak/sk to get access token
     */
    suspend fun getAccessToken(): JsonObject? {
        val params = mutableMapOf<String, Any>(
            "appKey" to getAppKey(), "appSecret" to getSecretKey()
        )
        return userApiService.getAccessToken(params)
    }

    /**
     * token/mobile to get user id
     */
    suspend fun getUseId(token: String, mobile: String): JsonObject? {
        return userApiService.getUseId(token, mobile)
    }

    @SuppressLint("DiscouragedApi")
    private fun getAppKey(): String {
        val resId = BaseApplication.getInstance().resources.getIdentifier(
            "DING_AK", "string", BaseApplication.getInstance().packageName
        )
        if (resId == 0) throw Resources.NotFoundException("Please define [DING_AK] in your root project gradle.properties file")
        return BaseApplication.getInstance().resources.getString(resId)
    }

    @SuppressLint("DiscouragedApi")
    private fun getSecretKey(): String {
        val resId = BaseApplication.getInstance().resources.getIdentifier(
            "DING_SK", "string", BaseApplication.getInstance().packageName
        )
        if (resId == 0) throw Resources.NotFoundException("Please define [DING_SK] in your root project gradle.properties file")
        return BaseApplication.getInstance().resources.getString(resId)
    }

    @SuppressLint("DiscouragedApi")
    private fun getAgentId(): String {
        val resId = BaseApplication.getInstance().resources.getIdentifier(
            "DING_AGENT", "string", BaseApplication.getInstance().packageName
        )
        if (resId == 0) throw Resources.NotFoundException("Please define [DING_AGENT] in your root project gradle.properties file")
        return BaseApplication.getInstance().resources.getString(resId)
    }

    @SuppressLint("DiscouragedApi")
    private fun getUsersId(): String {
        val resId = BaseApplication.getInstance().resources.getIdentifier(
            "DING_USERS", "string", BaseApplication.getInstance().packageName
        )
        if (resId == 0) throw Resources.NotFoundException("Please define [DING_USERS] in your root project gradle.properties file")
        return BaseApplication.getInstance().resources.getString(resId)
    }
}