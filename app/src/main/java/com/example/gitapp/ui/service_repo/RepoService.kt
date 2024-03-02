package com.example.gitapp.ui.service_repo

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.gitapp.MainApp
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.data.repository.Repository
import com.example.gitapp.entity.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RepoService : Service() {

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var repoNotificationCreator: RepoNotificationsCreator

    init {
        MainApp.appComponent.inject(this)
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, NotificationCompat.Builder(this@RepoService, CHANNEL_ID).build())
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.Main).launch {
            val differenceRepoList = getDifferenceRepoList()
            repoNotificationCreator.showNotifications(differenceRepoList)
            RepoAlarmHelper.setExactAlarm(this@RepoService, startAfterSec = 120)
            stopSelf()
        }

        return START_STICKY
    }

    private suspend fun getDifferenceRepoList(): List<Repo> {
        val favoriteRepos = repository.getFavoriteRepoList()
        val differenceRepoList = mutableListOf<Repo>()
        favoriteRepos.forEach { repo ->
            val fetchedRepo = getRepoFromApi(repo.owner.nameUser, repo.name)
            if (fetchedRepo != null) {
                fetchedRepo.stargazersCount = fetchedRepo.stargazersCount - repo.stargazersCount
                differenceRepoList.add(fetchedRepo)
            }
        }

        return differenceRepoList
    }

    private suspend fun getRepoFromApi(ownerName: String, repoName: String): RepoEntity? {
        try {
            return repository.getRepoFromApi(ownerName, repoName)
        } catch (e: UnknownHostException) {
            Log.e("service_error", "UnknownHostException: ${e.message} \n ${e.stackTrace.map { it.toString() + "\n" }}")
        } catch (e: RuntimeException) {
            Log.e("service_error", "RuntimeException: ${e.message} \n ${e.stackTrace.map { it.toString() + "\n" }}")
        } catch (e: SocketTimeoutException) {
            Log.e("service_error", "SocketTimeoutException: ${e.message} \n ${e.stackTrace.map { it.toString() + "\n" }}")
        }
        return null
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}