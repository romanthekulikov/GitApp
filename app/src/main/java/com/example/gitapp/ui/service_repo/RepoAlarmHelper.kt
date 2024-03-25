package com.example.gitapp.ui.service_repo

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.LocalDateTime
import java.time.ZoneId

object RepoAlarmHelper {

    private val backSkipPeriod = 21..23
    private val frontSkipPeriod = 0..8
    private val periodicity: Int =
        (24 - (backSkipPeriod.last - backSkipPeriod.first + frontSkipPeriod.last - frontSkipPeriod.first)) / 3

    fun setAlarm(context: Context, startAfterSec: Long = 0) {
        val alarmManager = context.getSystemService(Service.ALARM_SERVICE) as AlarmManager
        val receiverIntent = Intent(context, RepoReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val millisUntilNextAlarm = getMillisUntilNextAlarm(startAfterSec)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + millisUntilNextAlarm, pendingIntent)
            }
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + millisUntilNextAlarm, pendingIntent)
        }
    }

    private fun getMillisUntilNextAlarm(startAfterSec: Long): Long {
        val currentLocalDate = LocalDateTime.now()
        var millisUntilNextAlarm: Long
        val estimatedTime = if (startAfterSec == 0.toLong()) {
            millisUntilNextAlarm = (periodicity * 60 * 60 * 1000).toLong() // hours to milliseconds

            currentLocalDate.plusHours(periodicity.toLong())
        } else {
            millisUntilNextAlarm = startAfterSec * 1000

            currentLocalDate.plusSeconds(startAfterSec)
        }

        if ((estimatedTime.hour in backSkipPeriod) || (estimatedTime.hour in frontSkipPeriod)) {
            val alarmDateTime = if (estimatedTime.hour in backSkipPeriod) {
                // + 1 because frontSkipPeriod and backSkipPeriod ending at one number back
                currentLocalDate.plusDays(1).withHour(frontSkipPeriod.last + 1).withMinute(0)
            } else {
                currentLocalDate.withHour(frontSkipPeriod.last + 1).withMinute(0)
            }

            val alarmEpochSecond = alarmDateTime.atZone(ZoneId.systemDefault()).toEpochSecond()
            val t = currentLocalDate.atZone(ZoneId.systemDefault()).toEpochSecond()

            millisUntilNextAlarm = (alarmEpochSecond - t) * 1000
        }

        return millisUntilNextAlarm
    }
}