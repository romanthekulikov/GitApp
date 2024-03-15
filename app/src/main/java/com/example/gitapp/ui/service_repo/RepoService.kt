package com.example.gitapp.ui.service_repo

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.gitapp.App
import com.example.gitapp.R
import com.example.gitapp.data.repository.Repository
import com.example.gitapp.entity.Repo
import com.example.gitapp.ui.NotificationsCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

const val START_CHANNEL_ID = "start_repo_channel"
const val START_CHANNEL_NAME = "start_notification_channel"
const val START_NOTIFICATION_ID = 123

class RepoService : Service() {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var repoNotificationCreator: NotificationsCreator<Repo>

    init {
        App.appComponent.inject(this)
    }

    override fun onCreate() {
        super.onCreate()
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        repoNotificationCreator.createNotificationChannel(
            notificationManager = notificationManager,
            id = START_CHANNEL_ID,
            name = START_CHANNEL_NAME,
            description = this.getString(R.string.start_repo_channel_description)
        )
        val startNotification = NotificationCompat.Builder(this@RepoService, START_CHANNEL_ID)
            .setChannelId(START_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .build()
        startForeground(START_NOTIFICATION_ID, startNotification, FOREGROUND_SERVICE_TYPE_DATA_SYNC)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        scope.launch {
            RepoAlarmHelper.setAlarm(this@RepoService)
            val differenceRepoList = getDifferenceRepoList() // has repository with difference stargazersCount
            repoNotificationCreator.showNotifications(differenceRepoList)
            stopSelf()
        }

        return START_STICKY
    }

    private suspend fun getDifferenceRepoList(): List<Repo> {
        val favoriteRepos = repository.getFavoriteRepoList()
        val differenceRepoList = mutableListOf<Repo>()
        favoriteRepos.forEach{ repo ->
            try {
                val fetchedRepo = getRepoFromApi(repo.owner.nameUser, repo.name)
                fetchedRepo.stargazersCount = fetchedRepo.stargazersCount - repo.stargazersCount
                differenceRepoList.add(fetchedRepo)
                repository.makeRepoNotified(repo.owner.nameUser, repo.name)
            } catch (e: UnknownHostException) {
                Log.e("service_error", "UnknownHostException", e)
                startAlarmWithRepoList()
                return differenceRepoList
            } catch (e: SocketTimeoutException) {
                Log.e("service_error", "SocketTimeoutException: ", e)
                startAlarmWithRepoList()
                return differenceRepoList
            } catch (e: IOException) {
                Log.e("service_error", "IOException: ", e)
                startAlarmWithRepoList()
                return differenceRepoList
            } catch (e: HttpException) {
                Log.e("service_error", "HttpException: ", e)
                startAlarmWithRepoList()
                return differenceRepoList
            }
        }
        repository.makeReposNotNotified()
        return differenceRepoList
    }

    private suspend fun getRepoFromApi(ownerName: String, repoName: String): Repo {
        return repository.getRepoFromApi(ownerName, repoName)
    }
    private suspend fun startAlarmWithRepoList() {
        try {
            val limitResetTime = repository.getGithubResetLimitTime()
            val untilLimitResetTime = limitResetTime - (System.currentTimeMillis() / 1000)
            RepoAlarmHelper.setAlarm(this@RepoService, startAfterSec = untilLimitResetTime)
        } catch (e: UnknownHostException) {
            logErrorAndSetTriggerDefaultAlarm(e)
        } catch (e: SocketTimeoutException) {
            logErrorAndSetTriggerDefaultAlarm(e)
        } catch (e: IOException) {
            logErrorAndSetTriggerDefaultAlarm(e)
        } catch (e: HttpException) {
            logErrorAndSetTriggerDefaultAlarm(e)
        }
    }
    private fun logErrorAndSetTriggerDefaultAlarm(e: Exception) {
        Log.e("service_error", "On load reset limit: ", e)
        RepoAlarmHelper.setAlarm(this@RepoService)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}