package com.manna.monitor.room.export

/**
 * Define the database operation external call interface for operating HttpDataEntity
 */
interface HttpDataManage {
    /**
     * single insert
     */
    fun insert(data: HttpDataEntity)

    /**
     * batch insert
     */
    fun insertList(dataList: MutableList<HttpDataEntity>)
    fun delete(data: HttpDataEntity)
    fun deleteList(dataList: MutableList<HttpDataEntity>)
    fun update(data: HttpDataEntity)
    fun updateList(dataList: MutableList<HttpDataEntity>)

    /**
     * query by page
     */
    suspend fun queryPager(page: Int, pageSize: Int): MutableList<HttpDataEntity>

    /**
     * query by page and api path
     */
    suspend fun queryPagerWithPath(
        path: String,
        page: Int,
        pageSize: Int
    ): MutableList<HttpDataEntity>

    /**
     * Custom query
     */
    suspend fun queryCustom(queryFilter: QueryFilter): MutableList<HttpDataEntity>
}