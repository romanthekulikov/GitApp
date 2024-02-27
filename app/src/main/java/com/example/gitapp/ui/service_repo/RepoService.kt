package com.example.gitapp.ui.service_repo

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi

class RepoService : Service() {
    override fun onCreate() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        RepoAlarmHelper().setExactAlarm(this@RepoService, startAfterSec = 120)
        RepoNotificationsCreator(this@RepoService).createNotifications()
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}