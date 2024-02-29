package com.example.gitapp.ui

import android.app.NotificationChannel
import android.app.NotificationManager

abstract class NotificationsCreator {
    open suspend fun createNotifications() {}
    fun createNotificationChannel(notificationManager: NotificationManager, id: String, name: String) {
        val descriptionText = "channelIdDescription"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        channel.description = descriptionText
        notificationManager.createNotificationChannel(channel)
    }
}