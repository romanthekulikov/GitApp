package com.example.gitapp.ui.service_repo

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object RepoWorkerHelper {

    private val backSkipPeriod = 21..23
    private val frontSkipPeriod = 0..8
    private val periodicity: Int =
        (24 - (backSkipPeriod.last - backSkipPeriod.first + frontSkipPeriod.last - frontSkipPeriod.first)) / 3

    fun initWorker(context: Context, startAfterSec: Long = 0, repoForUpdates: Data = Data.Builder().build()) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<RepoWorker>()
            .setInitialDelay(
                duration = getSecUntilNextAlarm(startAfterSec),
                timeUnit = TimeUnit.SECONDS
            )
            .setConstraints(constraints)
            .setInputData(repoForUpdates)
            .build()
        val workManager = WorkManager.getInstance(context)

        workManager.enqueue(workRequest)
        workManager.getWorkInfoByIdLiveData(workRequest.id).observeForever {
            val data = it.outputData
            val repeatStartAfterSec = data.getLong("start_after_sec", 0)
            when (it.state) {
                WorkInfo.State.ENQUEUED -> { /* nothing */
                }

                WorkInfo.State.RUNNING -> { /* nothing */
                }

                WorkInfo.State.FAILED -> {
                    workManager.cancelWorkById(workRequest.id)
                    val repoForUpdate = data.getStringArray("repos")
                    if (repoForUpdate != null) {
                        initWorker(context, repeatStartAfterSec, data)
                    }
                }

                WorkInfo.State.SUCCEEDED -> {
                    workManager.cancelWorkById(workRequest.id)
                    initWorker(context, repeatStartAfterSec)
                }

                else -> { /* nothing */
                }
            }
        }

    }

    fun getWorkerData(repoForUpdates: List<String>, startAfterSec: Long): Data {
        var arrayRepo = arrayOf<String>()
        repoForUpdates.forEach { repo ->
            arrayRepo = arrayRepo.plus(repo)
        }
        val dataBuilder = Data.Builder()
        dataBuilder.putStringArray("repos", arrayRepo)
        dataBuilder.putLong("start_after_sec", startAfterSec)

        return dataBuilder.build()
    }

    private fun getSecUntilNextAlarm(startAfterSec: Long): Long {
        val currentLocalDate = LocalDateTime.now()
        var secUntilNextAlarm: Long
        val estimatedTime = if (startAfterSec == 0.toLong()) {
            secUntilNextAlarm = (periodicity * 60 * 60).toLong() // hours to seconds

            currentLocalDate.plusHours(periodicity.toLong())
        } else {
            secUntilNextAlarm = startAfterSec

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

            secUntilNextAlarm = (alarmEpochSecond - t)
        }

        return secUntilNextAlarm
    }
}