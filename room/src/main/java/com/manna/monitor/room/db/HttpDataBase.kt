package com.manna.monitor.room.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * db
 */
@Database(entities = [HttpData::class], version = 1, exportSchema = false)
abstract class HttpDataBase : RoomDatabase() {
    abstract fun httpDataDao(): HttpDataDao
}