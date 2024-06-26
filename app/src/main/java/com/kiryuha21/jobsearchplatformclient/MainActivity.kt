package com.kiryuha21.jobsearchplatformclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.kiryuha21.jobsearchplatformclient.data.local.AppDatabase
import com.kiryuha21.jobsearchplatformclient.data.service.createNotificationChannel
import com.kiryuha21.jobsearchplatformclient.data.service.enqueueTask
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationController
import com.kiryuha21.jobsearchplatformclient.ui.theme.JobSearchPlatformClientTheme
import com.kiryuha21.jobsearchplatformclient.util.askNotificationsPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch(Dispatchers.IO) {
            val now = Instant.now().epochSecond

            db.vacancyDAO.deleteExpiredVacancies(now)
            db.resumeDAO.deleteExpiredResumes(now)
        }

        askNotificationsPermission(this)
        createNotificationChannel(this)
        enqueueTask(this)

        setContent {
            JobSearchPlatformClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationController()
                }
            }
        }
    }
}
