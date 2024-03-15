package com.example.gitapp.ui.service_repo

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.gitapp.R
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.entity.Repo
import com.example.gitapp.ui.NotificationsCreator
import com.example.gitapp.ui.diagram.DiagramActivity

const val NOTIFICATION_ID = 33
private const val CHANNEL_NAME = "repo_push"
const val CHANNEL_ID = "repo_channel"

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class RepoNotificationsCreator(private val context: Context) : NotificationsCreator<Repo>() {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun showNotifications(dataList: List<Repo>) {
        createNotificationChannel(
            notificationManager,
            id = CHANNEL_ID,
            name = CHANNEL_NAME,
            description = context.getString(R.string.repo_channel_description)
        )
        val notificationMap = createNotificationMap(dataList)
        notificationMap.forEach { notification ->
            notificationManager.notify(notification.key, NOTIFICATION_ID, notification.value)
        }
    }

    override suspend fun createNotificationMap(dataList: List<Repo>): Map<String, Notification> {
        val map = mutableMapOf<String, Notification>()
        dataList.forEach { repo ->
            try {
                if (repo.stargazersCount > 0) {
                    map[repo.name] = getNewStarsRepoNotification((repo as ApiRepo).toRepoEntity(true))
                }
            } catch (e: ClassCastException) {
                Log.e("cast_ex", "Cast to ApiRepo error")
            }
        }

        return map
    }

    private fun getNewStarsRepoNotification(repo: RepoEntity): Notification {
        val intent = DiagramActivity.get(context, repo)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("GitApp: ${repo.owner.nameUser}")
            .setContentText("${repo.name} has ${repo.stargazersCount} new stars. Check them out now!⭐")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}