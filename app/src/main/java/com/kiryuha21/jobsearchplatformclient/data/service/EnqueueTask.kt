package com.kiryuha21.jobsearchplatformclient.data.service

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

const val WORK_NAME = "fetchNewJobApplications"

fun enqueueTask(context: Context) {
    val request = PeriodicWorkRequestBuilder<NewJobApplicationsWorker>(
        repeatInterval = 15,
        repeatIntervalTimeUnit = TimeUnit.MINUTES
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        WORK_NAME,
        ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
        request
    )
}