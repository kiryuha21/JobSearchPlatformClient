package com.kiryuha21.jobsearchplatformclient.data.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kiryuha21.jobsearchplatformclient.MainActivity
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.AppDataStore
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.jobApplicationRetrofit
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class NewJobApplicationsWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        val scope = CoroutineScope(Dispatchers.Default)

        for (i in 1L..15L * 60L / SECONDS_INTERVAL) {
            val areNotificationsOn = AppDataStore(context).getNotifications().stateIn(scope).value
            val isUserAuthorized = CurrentUser.info.role != UserRole.Undefined

            if (isUserAuthorized && areNotificationsOn) {
                val unseenApplications = withContext(Dispatchers.IO) {
                    networkCallWithReturnWrapper(
                        networkCall = {
                            jobApplicationRetrofit.countUnnotifiedJobApplications("Bearer ${AuthToken.getToken()}")
                        }
                    )
                }

                val resultIntent = Intent(context, MainActivity::class.java)
                val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                    addNextIntentWithParentStack(resultIntent)
                    getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                }

                if (unseenApplications != null && unseenApplications > 0) {
                    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle("SkillSift")
                        .setContentText("Вам поступило новых предложений : $unseenApplications")
                        .setSmallIcon(IconCompat.createWithResource(context, R.drawable.suitcase))
                        .setAutoCancel(true)
                        .setContentIntent(resultPendingIntent)
                        .build()

                    notificationManager.notify(NOTIFICATION_ID, notification)
                }
            }

            delay(1000L * SECONDS_INTERVAL)
        }

        return Result.success()
    }

    companion object {
        private const val NOTIFICATION_ID = 1 // can't be more than one notification at once
        private const val SECONDS_INTERVAL = 30L
    }
}
