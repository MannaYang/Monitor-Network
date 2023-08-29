package com.manna.monitor.report.work

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.auto.service.AutoService
import com.manna.monitor.report.export.WorkReport
import com.manna.monitor.room.export.QueryFilter
import com.manna.monitor.stone.BaseApplication
import com.manna.monitor.stone.http.GsonProvider

@AutoService(WorkReport::class)
class WorkProvider : WorkReport{

    private val workManager: WorkManager by lazy { WorkManager.getInstance(BaseApplication.getInstance()) }

    override fun reportData(queryFilter: QueryFilter, reportPlatform: Int) {
        val queryData = GsonProvider.gson.toJson(queryFilter)
        workManager.enqueue(
            OneTimeWorkRequestBuilder<ReportWork>().setInputData(
                Data.Builder().putString("filter", queryData).build()
            ).setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED)).build()
        )
    }
}