package com.example.gitapp.ui.service_repo

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build

object RepoAlarmHelper {
    fun setExactAlarm(context: Context, startAfterSec: Long) {
        val alarmManager = context.getSystemService(Service.ALARM_SERVICE) as AlarmManager
        val receiverIntent = Intent(context, RepoReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + startAfterSec * 1000, pendingIntent)
            }
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + startAfterSec * 1000, pendingIntent)
        }
    }

    fun isAlarmSet(context: Context): Boolean {
        val intent = Intent(context, RepoReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
        return pendingIntent != null
    }
}