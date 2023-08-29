package com.manna.monitor.room.db

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface HttpDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: HttpData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(dataList: List<HttpData>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(data: HttpData): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateList(dataList: List<HttpData>): Int

    @Delete
    fun delete(data: HttpData): Int

    @Delete
    fun deleteList(dataList: List<HttpData>): Int

    @RawQuery(observedEntities = [HttpData::class])
    suspend fun queryCustom(query: SupportSQLiteQuery): List<HttpData>

    @Query("SELECT * FROM http_data ORDER BY create_time DESC  LIMIT (:page-1)*:pageSize,:pageSize")
    suspend fun queryPager(page: Int, pageSize: Int = 20): List<HttpData>

    @Query("SELECT * FROM http_data WHERE  url = :path ORDER BY create_time DESC  LIMIT (:page-1)*:pageSize,:pageSize")
    suspend fun queryPagerWithPath(path: String, page: Int, pageSize: Int = 20): List<HttpData>
}