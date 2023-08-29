package com.manna.monitor.room

import com.manna.monitor.room.db.HttpData
import com.manna.monitor.room.db.HttpDataDao
import com.manna.monitor.room.export.HttpDataEntity

/**
 * HttpDataDao external operation
 */
object HttpDataDaoProvider {

    private lateinit var httpDataDao: HttpDataDao

    fun getHttpDataDao(): HttpDataDao {
        return httpDataDao
    }

    fun setHttpDataDao(httpDataDao: HttpDataDao) {
        this.httpDataDao = httpDataDao
    }

    /**
     * Mapping To Table
     */
    fun mappingToTable(data: HttpDataEntity): HttpData {
        return HttpData(
            data.appName,
            data.versionName,
            data.env,
            data.url,
            data.params,
            data.result,
            data.errorInfo,
            data.success,
            data.requestTime,
            data.costTime,
            data.httpCode,
            data.networkInfo,
            data.device,
            data.token,
            data.userName,
            data.phone,
            data.reportType
        ).apply {
            id = data.id ?: 0
        }
    }

    /**
     * Mapping To Entity
     */
    fun mappingToEntity(data: HttpData): HttpDataEntity {
        return HttpDataEntity(
            data.appName,
            data.versionName,
            data.env,
            data.url,
            data.params,
            data.result,
            data.errorInfo,
            data.success,
            data.requestTime,
            data.costTime,
            data.httpCode,
            data.networkInfo,
            data.device,
            data.token,
            data.userName,
            data.phone,
            data.reportType,
            data.createTime,
            data.id
        )
    }
}