package com.otus.services

import android.annotation.SuppressLint
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {

    private val serviceStatusLabel by lazy { findViewById<TextView>(R.id.service_status) }
    private val answerButton by lazy { findViewById<Button>(R.id.get_answer) }
    private val answerText by lazy { findViewById<TextView>(R.id.magic_answer) }

    private var iRemoteService: IMyAidlInterface? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceStatusLabel.text = "Connected"
            iRemoteService = IMyAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceStatusLabel.text = "Disconnected"
            iRemoteService = null
        }
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val job =
            JobInfo.Builder(1, ComponentName(this, MyJobService::class.java)).setPeriodic(1000)
                .build()
        val jobScheduler = getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(job)
    }
}