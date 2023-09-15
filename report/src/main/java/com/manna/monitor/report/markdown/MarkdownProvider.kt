package com.manna.monitor.report.markdown

import android.util.Log
import com.manna.monitor.room.export.HttpDataEntity
import com.manna.monitor.stone.http.GsonProvider
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Generate Markdown content
 */
object MarkdownProvider {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.CHINA)

    fun generateMarkdownContent(dataList: List<HttpDataEntity>, maxLength: Int): List<String> {
        Log.d("Monitor", "Generate markdown content starting")
        val contentList = mutableListOf<String>()
        val headerStr = generateHeader(dataList)
        Log.d(
            "Monitor",
            "Generate markdown header content : ${GsonProvider.gson.toJson(headerStr)}"
        )
        val groups = dataList.groupBy { it.url }
        val builderList = mutableListOf<MarkdownBuilder>()
        groups.forEach { (url, dataList) ->
            builderList.ifEmpty { builderList.add(MarkdownBuilder()) }
            var builder = builderList.last()
            if (headerStr.length + builder.generate().length > maxLength) {
                builder = MarkdownBuilder()
                builderList.add(builder)
            }
            val urlContent = generateUrl(url, dataList)
            Log.d(
                "Monitor",
                "Generate markdown api url content : ${GsonProvider.gson.toJson(urlContent)}"
            )
            builder.append(urlContent)
            dataList.sortedBy { it.createTime }.forEach { data ->
                val responseContent = generateResponse(data)
                Log.d(
                    "Monitor",
                    "Generate markdown response content : ${GsonProvider.gson.toJson(responseContent)}"
                )
                builder.append(responseContent)
            }
            val countContent = generateCount(dataList)
            Log.d(
                "Monitor",
                "Generate markdown count content : ${GsonProvider.gson.toJson(countContent)}"
            )
            builder.append(countContent)
        }
        //Sealed content list - String
        builderList.forEach { bodyBuilder ->
            contentList.add(headerStr + bodyBuilder.build())
        }
        Log.d("Monitor", "Generate markdown content finished")
        return contentList
    }

    /**
     * Generate header content
     */
    private fun generateHeader(dataList: List<HttpDataEntity>): String {
        return MarkdownBuilder().title(
            3,
            "[${dataList[0].appName}] [${dataList[0].versionName}] (${dataList[0].env}) \n${dataList[0].device} "
        ).newLine().title(
            6, MarkdownBuilder().color(
                MarkdownColor.USER,
                "User:${dataList[0].userName} \nPhone:${dataList[0].phone} \nToken:${dataList[0].token}"
            ).build()
        ).build()
    }

    /**
     * Generate url content
     */
    private fun generateUrl(urlPath: String, dataList: List<HttpDataEntity>): StringBuilder {
        return MarkdownBuilder().title(
            2,
            MarkdownBuilder().color(MarkdownColor.URL, urlPath).newLine()
                .title(5, "Body: ${dataList.find { !it.params.isNullOrEmpty() }?.params}").newLine()
                .build()
        ).generate()
    }

    /**
     * Generate response content
     */
    private fun generateResponse(data: HttpDataEntity): StringBuilder {
        val color = if (data.success) MarkdownColor.SUCCESS else MarkdownColor.FAIL
        val prefix = if (data.success) "[Success]" else "[Fail]"
        return MarkdownBuilder().newLine().content(
            MarkdownBuilder().color(color, prefix)
                .build() + " Record: ${dateFormat.format(data.requestTime)} Cost：${data.costTime} ms"
        ).newLine().quote("Result: ${data.result}").generate()
    }

    /**
     * Generate success \ fail count content
     */
    private fun generateCount(dataList: List<HttpDataEntity>): StringBuilder {
        return MarkdownBuilder().newLine().newLine().title(
            6, MarkdownBuilder().color(
                MarkdownColor.COUNT,
                "Success: ${dataList.filter { it.success }.size}, Fail: ${dataList.filter { !it.success }.size}"
            ).build()
        ).line().generate()
    }
}