package com.kiryuha21.jobsearchplatformclient.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE

const val NOTIFICATION_CHANNEL_ID = "NewJobApplicationsChannelId"
const val NOTIFICATION_CHANNEL_NAME = "NewJobApplicationsChannel"
const val NOTIFICATION_CHANNEL_DESCRIPTION = "Sends notification for every new found job application for current user every 30 seconds"

fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = NOTIFICATION_CHANNEL_DESCRIPTION
    }

    val notificationManager: NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}