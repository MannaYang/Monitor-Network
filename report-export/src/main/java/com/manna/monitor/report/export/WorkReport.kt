package com.manna.monitor.report.export

import com.manna.monitor.room.export.QueryFilter

interface WorkReport {
    /**
     * Report http data
     * @param queryFilter [com.manna.monitor.room.export.QueryFilter]
     * @param reportPlatform Platform source,such as DingTalk(1) 、Server(2) 、Others(3)
     */
    fun reportData(queryFilter: QueryFilter, reportPlatform: Int)
}