package com.manna.monitor.room.export

/**
 * Custom query conditions
 */
data class QueryFilter(
    //conditions : >
    val costTime: Long = 0L,
    //conditions : =
    val url: String = "",
    val env: String = "",
    val appName: String = "",
    val versionName: String = "",
    val userName: String = "",
    val phone: String = "",
    val reportType: Int = 0,
    val maxCount: Int = 50,
    //conditions : like
    val device: String = "",
    //conditions : time interval
    val startTime: Long = 0L,
    val endTime: Long = 0L,
)
