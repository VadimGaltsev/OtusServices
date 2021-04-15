package com.otus.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class RemoteService : Service() {

    private val random = Random(0)
    private var circle: Circle? = null

    private val binder = object : IMyAidlInterface.Stub() {

        override fun getMagicNumber() = random.nextInt()

        override fun fillCircle(circle: Circle?) {
            this@RemoteService.circle = circle?.apply {
                radius = 10
                x = 100
                y = 100
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return when (intent?.getIntExtra("who", 0)) {
            0 -> binder
            else -> Messenger(handler).binder
        }
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)

    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        return super.onUnbind(intent)
    }

    val remoteViews by lazy { RemoteViews(packageName, R.layout.notification) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val newServiceIntent = Intent(this, MyIntentService::class.java)
            .putExtra(
                "Intent",
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity2::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
        remoteViews.setOnClickPendingIntent(
            R.id.click,
            PendingIntent.getService(this, 0, newServiceIntent, PendingIntent.FLAG_ONE_SHOT)
        )
        Log.d("OtusService", "SHOW NOTIFY $startId")
        sendNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("NewApi")
    private fun sendNotification() {
        val notification = NotificationCompat.Builder(this, "CircleChannel")
            .setCustomBigContentView(remoteViews)
            .setChannelId("CircleChannel")
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        val channel = NotificationChannel(
            "CircleChannel",
            "CircleChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(this).apply {
            createNotificationChannel(channel)
            notify(1, notification)
        }
    }

    val handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            this@RemoteService.remoteViews.setTextViewText(R.id.some_text, msg.arg1.toString())
            sendNotification()
        }
    }
}