package com.manna.monitor.stone.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


/**
 * At the top level of your kotlin file
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "monitor_network")

suspend fun getStringValue(key: String): Flow<String> = DataStoreProvider.instance.data.catch {
    Log.d("DataStoreProvider", it.stackTraceToString())
    emit(emptyPreferences())
}.map { it[stringPreferencesKey(key)] ?: "" }

suspend fun setStringValue(key: String, value: String) =
    DataStoreProvider.instance.edit { mutablePreferences ->
        mutablePreferences[stringPreferencesKey(key)] = value
    }

suspend fun getLongValue(key: String): Flow<Long> = DataStoreProvider.instance.data.catch {
    Log.d("DataStoreProvider", it.stackTraceToString())
    emit(emptyPreferences())
}.map { it[longPreferencesKey(key)] ?: 0L }

suspend fun setLongValue(key: String, value: Long) =
    DataStoreProvider.instance.edit { mutablePreferences ->
        mutablePreferences[longPreferencesKey(key)] = value
    }

/**
 * Cache data
 */
class DataStoreProvider private constructor() {

    companion object {
        private var dataStore: DataStore<Preferences>? = null
        val instance by lazy { dataStore!! }

        /**
         * you should init it in [com.manna.monitor.stone.lifecycle.ApplicationLifecycleProxy]
         */
        fun initDataStore(context: Context) {
            dataStore ?: synchronized(this) { dataStore = context.dataStore }
        }
    }
}