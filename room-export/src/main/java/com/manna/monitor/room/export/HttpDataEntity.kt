package com.manna.monitor.room.export

/**
 * DataBase Table Mapping To Entity
 * @see com.manna.monitor.room.db.HttpData
 * @see com.manna.monitor.room.HttpDataDaoProvider
 */
data class HttpDataEntity(
    var appName: String,
    var versionName: String,
    var env: String,
    var url: String,
    var params: String?,
    val result: String?,
    var errorInfo: String?,
    var success: Boolean,
    var requestTime: Long,
    var costTime: Long,
    var httpCode: String,
    var networkInfo: String?,
    var device: String?,
    var token: String?,
    var userName: String?,
    var phone: String?,
    var reportType: Int,
    val createTime: Long?,
    val id: Long?
)
