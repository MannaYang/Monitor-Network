package com.manna.monitor.room.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Define TableName and Table ColumnInfo
 */
@Entity(tableName = "http_data")
data class HttpData(
    @ColumnInfo(name = "app_name") var appName: String,
    @ColumnInfo(name = "version_name") var versionName: String,
    /**
     * http environment
     * dev、test、prod
     */
    @ColumnInfo(name = "env") var env: String,
    /**
     * http url path
     */
    @ColumnInfo(name = "url") var url: String,
    /**
     * request params
     */
    @ColumnInfo(name = "params") var params: String?,
    /**
     * response result
     */
    @ColumnInfo(name = "result") var result: String?,
    /**
     * error info
     */
    @ColumnInfo(name = "error_info") var errorInfo: String?,
    /**
     * request is ture or false
     */
    @ColumnInfo(name = "success") var success: Boolean,
    /**
     * request time , milliseconds
     */
    @ColumnInfo(name = "request_time") var requestTime: Long,
    /**
     * cost time , milliseconds
     */
    @ColumnInfo(name = "cost_time") var costTime: Long,
    /**
     * http status code,200,404,500
     */
    @ColumnInfo(name = "http_code") var httpCode: String,
    /**
     * network_info : WIFI,4G,5G
     */
    @ColumnInfo(name = "network_info") var networkInfo: String?,
    /**
     * device info : Android 12，HUAWEI P40 Pro
     */
    @ColumnInfo(name = "device") var device: String?,
    /**
     * user token
     */
    @ColumnInfo(name = "token") var token: String?,
    /**
     * user name
     */
    @ColumnInfo(name = "user_name") var userName: String?,
    /**
     * user phone
     */
    @ColumnInfo(name = "phone") var phone: String?,
    /**
     * report type ,
     * 0 UnReport ,
     * 1 report DingTalk ,
     * 2 report Server
     */
    @ColumnInfo(name = "report_type", defaultValue = "0") var reportType: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "create_time")
    var createTime: Long = System.currentTimeMillis()

    override fun toString(): String {
        return "HttpData(appName='$appName', versionName='$versionName', reportType=$reportType, id=$id, createTime=$createTime),env='$env', url='$url', params=$params, errorInfo=$errorInfo, success=$success, requestTime=$requestTime, costTime=$costTime, httpCode='$httpCode', networkInfo=$networkInfo, device=$device, token=$token, username=$userName, phone=$phone"
    }
}
