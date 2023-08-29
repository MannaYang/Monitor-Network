package com.manna.monitor.network.interceptor

import android.os.Build
import android.util.Log
import com.google.auto.service.AutoService
import com.manna.monitor.http.export.HttpDataResult
import com.manna.monitor.http.export.HttpInterceptorListener
import com.manna.monitor.room.export.HttpDataEntity
import com.manna.monitor.room.export.HttpDataManage
import java.util.ServiceLoader

@AutoService(HttpInterceptorListener::class)
class HttpInterceptor : HttpInterceptorListener {

    private val loaderProxy by lazy { ServiceLoader.load(HttpDataManage::class.java) }

    override fun onIntercept(httpData: HttpDataResult) {
        Log.d("Monitor", "Intercept Okhttp request data : \n$httpData")
        loaderProxy.first().insert(sealRoomData(httpData))
    }

    private fun sealRoomData(dataResult: HttpDataResult): HttpDataEntity {
        Log.d("Monitor", "Sealed room data and insert to database")
        return HttpDataEntity(
            //your app name
            appName = "Monitor-Network",
            //your app version name , it is defined in app/build.gradle file
            versionName = "1.0.0",
            //your app host environment
            env = "TEST",
            //interface url
            url = dataResult.url,
            //request body
            params = dataResult.body,
            //response result, if this field is exist and if necessary
            result = dataResult.data,
            //response code, if this field is exist and if necessary
            errorInfo = dataResult.code,
            //response is success, if this field is exist and if necessary
            success = dataResult.success,
            //request start time
            requestTime = dataResult.startTime,
            costTime = dataResult.endTime - dataResult.startTime,
            httpCode = dataResult.httpCode.toString(),
            //get your devices network type ,such as WIFI,4G,5G
            networkInfo = "5G",
            //devices info
            device = "${Build.BRAND} ${Build.MODEL} Android ${Build.VERSION.RELEASE}",
            token = "your account token",
            userName = "your account name",
            phone = "your account phone number",
            //when inserting data,this value is 0,means data not reported
            // when report data,this value is 1 or 2 ,the 1 : report DingTalk ,the 2 : report Server
            //you can custom define it
            reportType = 0,
            //data record time
            createTime = System.currentTimeMillis(),
            //autoincrement,no care about
            id = null
        )
    }
}