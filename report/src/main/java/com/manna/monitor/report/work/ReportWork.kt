package com.manna.monitor.report.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.manna.monitor.report.dingtalk.DingProvider
import com.manna.monitor.report.markdown.MarkdownProvider
import com.manna.monitor.room.export.HttpDataManage
import com.manna.monitor.room.export.QueryFilter
import com.manna.monitor.stone.http.GsonProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ServiceLoader

/**
 * WorkManager Report
 */
class ReportWork(context: Context, private val workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val reportWork = CoroutineScope(Dispatchers.IO)
    private val maxLength = 20000
    private val loaderProxy by lazy { ServiceLoader.load(HttpDataManage::class.java) }

    override fun doWork(): Result {

        val filterJson = workerParams.inputData.getString("filter")
        if (filterJson.isNullOrEmpty()) return Result.failure()
        val queryFilter = GsonProvider.gson.fromJson(filterJson, QueryFilter::class.java)

        Log.d("Monitor", "Report work query conditions : $queryFilter ")

        reportWork.launch {
            //Query all data according to query filter conditions
            val dataList = loaderProxy.first().queryCustom(queryFilter)
            Log.d("Monitor", "Report work query result : ${GsonProvider.gson.toJson(dataList)} ")
            if (dataList.isEmpty()) {
                Log.d("Monitor", "Report work query result : dataList is null or empty")
                return@launch
            }
            //mark report
            var hasReport = false
            //format data
            val formatList = MarkdownProvider.generateMarkdownContent(dataList, maxLength)
            formatList.forEach { formatData ->
                Log.d(
                    "Monitor",
                    "Report work format data : \n${GsonProvider.gson.toJson(formatData)}"
                )
                //This is DingTalk report , you can custom it like Server report or others
                DingProvider.instance.reportWorkSpace("Network-Monitor", formatData) {
                    Log.d("Monitor", "Report work space success : $it")
                    if (it && !hasReport) {
                        hasReport = true
                        dataList.forEach { data -> data.reportType = 1 }
                        loaderProxy.first().deleteList(dataList)
                    }
                }

                //Server report :
                //DingProvider.instance.reportDataServer(){ }

                //Other report :
                //DingProvider.instance.reportDataOther(){ }
            }
        }
        return Result.success()
    }
}