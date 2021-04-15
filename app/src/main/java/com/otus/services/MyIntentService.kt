package com.otus.services

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.Context
import android.content.ServiceConnection
import android.os.*

class MyIntentService : IntentService("MyIntentService") {

    var messenger: Messenger? = null

    val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            messenger = null
        }
    }

    var counter = 0

    @SuppressLint("NewApi")
    override fun onHandleIntent(intent: Intent?) {
        bindService(
            Intent(this, RemoteService::class.java).putExtra("who", 1),
            connection,
            Service.BIND_AUTO_CREATE
        )
        val pendingIntent = intent?.getParcelableExtra<PendingIntent>("Intent")
        while (true) {
            messenger?.send(Message.obtain()
                .apply { this.arg1 = counter++ })
            Thread.sleep(1000)
            if (counter == 5) {
                unbindService(connection)
                val alarmManager = getSystemService(AlarmManager::class.java)
                alarmManager.set(
                    AlarmManager.RTC,
                    System.currentTimeMillis() + 4000,
                    PendingIntent.getService(
                        this,
                        0,
                        Intent(this, RemoteService::class.java),
                        PendingIntent.FLAG_ONE_SHOT
                    )
                );
                break
            }
        }
        pendingIntent?.send()
    }
}