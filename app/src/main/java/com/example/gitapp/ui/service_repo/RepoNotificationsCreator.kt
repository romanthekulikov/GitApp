package com.example.gitapp.ui.service_repo

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.gitapp.MainApp
import com.example.gitapp.R
import com.example.gitapp.data.api.GitApiService
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.data.repository.Repository
import com.example.gitapp.ui.NotificationsCreator
import com.example.gitapp.ui.diagram.DiagramActivity
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

const val NOTIFICATION_ID = 33
const val CHANNEL_ID = "channelID"

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class RepoNotificationsCreator(private val context: Context) : NotificationsCreator() {
    init {
        MainApp.appComponent.inject(this)
    }

    @Inject
    lateinit var apiService: GitApiService

    @Inject
    lateinit var repository: Repository

    override suspend fun createNotifications() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager, id = "repo_id", name = "repo_push")
        val tagToNotificationMap = getTagToFavoriteRepoNewStarsNotificationMap()
        tagToNotificationMap.forEach { notification ->
            notificationManager.notify(notification.key, NOTIFICATION_ID, notification.value)
        }
    }

    private suspend fun getTagToFavoriteRepoNewStarsNotificationMap(): Map<String, Notification> {
        val favoriteRepoList = repository.getFavoriteRepoList()
        val map = mutableMapOf<String, Notification>()
        favoriteRepoList.forEach { repo ->
            val fetchedRepo = getRepoFromApi(repo.owner.nameUser, repo.name)
            var newStarsCount = 0

            if (fetchedRepo != null) {
                newStarsCount = fetchedRepo.stargazersCount - repo.stargazersCount
            }

            if (newStarsCount > 0) {
                map[repo.name] = getNewStarsRepoNotification(fetchedRepo!!, newStarsCount)
            }
        }

        return map
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

    private fun getNewStarsRepoNotification(repo: RepoEntity, newStarsCount: Int): Notification {
        val intent = DiagramActivity.get(context, repo)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("GitApp: ${repo.owner.nameUser}")
            .setContentText("${repo.name} has $newStarsCount new stars. Check them out now!⭐")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}