package com.manna.monitor.report.dingtalk

import android.util.Log
import com.manna.monitor.stone.data.getLongValue
import com.manna.monitor.stone.data.getStringValue
import com.manna.monitor.stone.data.setLongValue
import com.manna.monitor.stone.data.setStringValue
import kotlinx.coroutines.flow.first

class DingProvider {

    companion object {
        val instance = DingProvider()
    }

    /**
     * report work space
     */
    suspend fun reportWorkSpace(title: String, content: String, success: (Boolean) -> Unit) {
        kotlin.runCatching {
            var token = getStringValue("ding-token").first()
            val expiredTime = getLongValue("ding-expired").first()
            if (token.isEmpty() || System.currentTimeMillis() > expiredTime) {
                Log.d("Monitor", "Report token is empty or expired")
                val data = DingRepository.instance.getAccessToken()
                Log.d("Monitor", "Get access token : $data")
                token = data?.get("accessToken")?.asString ?: ""
                val expired = data?.get("expireIn")?.asLong ?: 0L
                Log.d("Monitor", "Cache access token and expired time")
                setStringValue("ding-token", token)
                setLongValue("ding-expired", expired)
            }
            Log.d("Monitor", "Starting async report work space")
            val jsonData = DingRepository.instance.asyncReport(token, title, content)
            Log.d("Monitor", "Finish async report work space, jsonData : $jsonData")
            val errorCode = jsonData?.get("errcode")?.asInt ?: -1
            success(errorCode == 0)
        }
    }

    suspend fun reportDataServer(title: String, content: String, success: (Boolean) -> Unit) {
        //do something
    }

    suspend fun reportDataOther(title: String, content: String, success: (Boolean) -> Unit) {
        //do something
    }
}