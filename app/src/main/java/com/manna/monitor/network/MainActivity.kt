package com.manna.monitor.network

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.manna.monitor.network.model.MainViewModel
import com.manna.monitor.report.export.WorkReport
import com.manna.monitor.room.export.QueryFilter
import java.util.ServiceLoader

class MainActivity : AppCompatActivity() {

    private val loaderProxy by lazy { ServiceLoader.load(WorkReport::class.java) }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initData()
        findViewById<TextView>(R.id.tv_request).setOnClickListener {
            viewModel.testPost()
        }

        findViewById<TextView>(R.id.tv_report).setOnClickListener {
            initData()
        }
    }

    private fun initData() {
        val queryFilter = QueryFilter(startTime = 0, endTime = System.currentTimeMillis())
        loaderProxy.first().reportData(queryFilter, 1)
    }
}