package com.example.gitapp.ui.service_repo

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object RepoWorkerHelper {

    private val backSkipPeriod = 21..23
    private val frontSkipPeriod = 0..8
    private val periodicity: Int =
        (24 - (backSkipPeriod.last - backSkipPeriod.first + frontSkipPeriod.last - frontSkipPeriod.first)) / 3

    fun initWorker(context: Context, startAfterSec: Long = 0, repoForUpdates: List<String> = listOf()) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<RepoWorker>()
            .setInitialDelay(
                duration = getSecUntilNextAlarm(startAfterSec),
                timeUnit = TimeUnit.SECONDS
            )
            .setConstraints(constraints)
            .setInputData(getWorkerData(repoForUpdates))
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun getWorkerData(repoForUpdates: List<String>): Data {
        var arrayRepo = arrayOf<String>()
        repoForUpdates.forEach { repo ->
            arrayRepo = arrayRepo.plus(repo)
        }
        val dataBuilder = Data.Builder()
        dataBuilder.putStringArray("repos", arrayRepo)

        return dataBuilder.build()
    }

    private fun getSecUntilNextAlarm(startAfterSec: Long): Long {
        val currentLocalDate = LocalDateTime.now()
        var millisUntilNextAlarm: Long
        val estimatedTime = if (startAfterSec == 0.toLong()) {
            millisUntilNextAlarm = (periodicity * 60 * 60).toLong() // hours to seconds

            currentLocalDate.plusHours(periodicity.toLong())
        } else {
            millisUntilNextAlarm = startAfterSec

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

            millisUntilNextAlarm = (alarmEpochSecond - t)
        }

        return millisUntilNextAlarm
    }
}