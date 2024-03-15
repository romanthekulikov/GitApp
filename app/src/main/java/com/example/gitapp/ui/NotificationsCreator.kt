package com.example.gitapp.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager

abstract class NotificationsCreator<T> {
    abstract suspend fun showNotifications(dataList: List<T>)

    protected abstract suspend fun createNotificationMap(dataList: List<T>): Map<String, Notification>

    fun createNotificationChannel(notificationManager: NotificationManager, id: String, name: String, description: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        notificationManager.createNotificationChannel(channel)
    }
}