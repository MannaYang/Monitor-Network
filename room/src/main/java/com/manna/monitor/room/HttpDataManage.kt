package com.manna.monitor.room

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.auto.service.AutoService
import com.manna.monitor.room.db.HttpData
import com.manna.monitor.room.db.HttpDataDao
import com.manna.monitor.room.export.HttpDataEntity
import com.manna.monitor.room.export.HttpDataManage
import com.manna.monitor.room.export.QueryFilter
import com.manna.monitor.stone.http.GsonProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * database [insert update delete query]
 * @see com.manna.monitor.room.export.HttpDataManage
 */
@AutoService(HttpDataManage::class)
class HttpDataManage : HttpDataManage {

    private val httpDataDao: HttpDataDao by lazy { HttpDataDaoProvider.getHttpDataDao() }
    private val httpDataJob = CoroutineScope(Dispatchers.IO)

    override fun insert(data: HttpDataEntity) {
        Log.d("Monitor", "Starting insert data : \n${GsonProvider.gson.toJson(data)}")
        val result = HttpDataDaoProvider.mappingToTable(data)
        httpDataJob.launch {
            httpDataDao.insert(result)
            Log.d("Monitor", "Finished insert data")
        }
    }

    override fun insertList(dataList: MutableList<HttpDataEntity>) {
        if (dataList.isEmpty()) return
        Log.d("Monitor", "Starting insert data list : \n$dataList")
        val result = mutableListOf<HttpData>().apply {
            dataList.forEach {
                val result = HttpDataDaoProvider.mappingToTable(it)
                add(result)
            }
        }
        httpDataJob.launch {
            httpDataDao.insertList(result)
            Log.d("Monitor", "Finished insert data list :  ${result.size} results affected")
        }
    }

    override fun delete(data: HttpDataEntity) {
        if (data.id == null) {
            Log.d("Monitor", "Delete error : [data.id] is null")
            return
        }
        Log.d("Monitor", "Starting delete data : $data")
        val result = HttpDataDaoProvider.mappingToTable(data)
        httpDataJob.launch {
            val count = httpDataDao.delete(result)
            Log.d("Monitor", "Finished delete data : $count results affected")
        }
    }

    override fun deleteList(dataList: MutableList<HttpDataEntity>) {
        if (dataList.isEmpty()) return
        Log.d("Monitor", "Starting delete data list : ${GsonProvider.gson.toJson(dataList)}")
        val result = mutableListOf<HttpData>().apply {
            dataList.forEach {
                if (it.id != null) {
                    val result = HttpDataDaoProvider.mappingToTable(it)
                    add(result)
                } else {
                    Log.d("Monitor", "Delete error : [it.id] is null")
                }
            }
        }
        httpDataJob.launch {
            val count = httpDataDao.deleteList(result)
            Log.d(
                "Monitor",
                "Finished delete data list : $count results affected,total: ${result.size}"
            )
        }
    }

    override fun update(data: HttpDataEntity) {
        if (data.id == null) {
            Log.d("Monitor", "Update error : [data.id] is null")
            return
        }
        Log.d("Monitor", "Starting update data : $data")
        val result = HttpDataDaoProvider.mappingToTable(data)
        httpDataJob.launch {
            val count = httpDataDao.update(result)
            Log.d("Monitor", "Finished update data : $count results affected")
        }
    }

    override fun updateList(dataList: MutableList<HttpDataEntity>) {
        if (dataList.isEmpty()) return
        Log.d("Monitor", "Starting update data list : $dataList")
        val result = mutableListOf<HttpData>().apply {
            dataList.forEach {
                if (it.id != null) {
                    val result = HttpDataDaoProvider.mappingToTable(it)
                    add(result)
                } else {
                    Log.d("Monitor", "Update error : [it.id] is null")
                }
            }
        }
        httpDataJob.launch {
            val count = httpDataDao.updateList(result)
            Log.d(
                "Monitor",
                "Finished update data list : $count results affected,total: ${result.size}"
            )
        }
    }

    /**
     * Default query by pager
     */
    override suspend fun queryPager(page: Int, pageSize: Int): MutableList<HttpDataEntity> {
        val actualPage = if (page < 1) 1 else page
        val actualPageSize = if (pageSize < 1) 20 else pageSize
        val list = httpDataDao.queryPager(actualPage, actualPageSize)
        Log.d("Monitor", "Starting query pager : page: $actualPage , pageSize: $actualPageSize")
        val result = mutableListOf<HttpDataEntity>().apply {
            list.forEach {
                val data = HttpDataDaoProvider.mappingToEntity(it)
                add(data)
            }
        }
        Log.d("Monitor", "Finished query pager : $result")
        return result
    }

    /**
     * Query by url path
     */
    override suspend fun queryPagerWithPath(
        path: String, page: Int, pageSize: Int
    ): MutableList<HttpDataEntity> {
        val actualPage = if (page < 1) 1 else page
        val actualPageSize = if (pageSize < 1) 20 else pageSize
        if (path.isEmpty()) return queryPager(actualPage, actualPageSize)
        Log.d(
            "Monitor",
            "Starting query pager with path : $path, page: $actualPage , pageSize: $actualPageSize"
        )
        val list = httpDataDao.queryPagerWithPath(path, actualPage, actualPageSize)
        val result = mutableListOf<HttpDataEntity>().apply {
            list.forEach {
                val data = HttpDataDaoProvider.mappingToEntity(it)
                add(data)
            }
        }
        Log.d("Monitor", "Finished query pager with path : $result")
        return result
    }

    /**
     * Please handle according to actual needs，
     * a sql example like this : [SELECT * FROM http_data ORDER BY create_time DESC]
     */
    override suspend fun queryCustom(queryFilter: QueryFilter): MutableList<HttpDataEntity> {
        var sql = "SELECT * FROM http_data WHERE report_type = ${queryFilter.reportType}"
        if (queryFilter.costTime > 0L) sql = sql.plus(" AND cost_time > ${queryFilter.costTime}")
        if (queryFilter.url.isNotEmpty()) sql = sql.plus(" AND url = ${queryFilter.url}")
        if (queryFilter.env.isNotEmpty()) sql = sql.plus(" AND env = ${queryFilter.env}")
        if (queryFilter.appName.isNotEmpty()) sql =
            sql.plus(" AND app_name = ${queryFilter.appName}")
        if (queryFilter.versionName.isNotEmpty()) sql =
            sql.plus(" AND version_name = ${queryFilter.versionName}")
        if (queryFilter.userName.isNotEmpty()) sql =
            sql.plus(" AND user_name = ${queryFilter.userName}")
        if (queryFilter.phone.isNotEmpty()) sql = sql.plus(" AND phone = ${queryFilter.phone}")
        if (queryFilter.device.isNotEmpty()) sql =
            sql.plus(" AND device LIKE %${queryFilter.device}%")
        if (queryFilter.startTime >= 0) sql =
            sql.plus(" AND create_time > ${queryFilter.startTime}")
        sql = if (queryFilter.endTime > 0) sql.plus(" AND create_time < ${queryFilter.endTime}")
        else sql.plus(" AND create_time < ${System.currentTimeMillis()}")
        sql = sql.plus(" ORDER BY create_time DESC LIMIT 0,${queryFilter.maxCount}")
        Log.d("Monitor", "Starting query custom sql : $sql")
        val list = httpDataDao.queryCustom(SimpleSQLiteQuery(sql))
        val result = mutableListOf<HttpDataEntity>().apply {
            list.forEach {
                val data = HttpDataDaoProvider.mappingToEntity(it)
                add(data)
            }
        }
        Log.d("Monitor", "Finished query custom sql result : ${GsonProvider.gson.toJson(result)}")
        return result
    }
}