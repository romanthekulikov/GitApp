package com.example.gitapp.ui.service_repo

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class RepoAlarmHelper {
    @RequiresApi(Build.VERSION_CODES.S)
    fun setExactAlarm(context: Context, startAfterSec: Long) {
        val alarmManager = context.getSystemService(Service.ALARM_SERVICE) as AlarmManager
        val receiverIntent = Intent(context, RepoReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + startAfterSec * 1000, pendingIntent)
        }
    }
}