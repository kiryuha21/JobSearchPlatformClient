package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import com.kiryuha21.jobsearchplatformclient.data.domain.NavigationDrawerItem

val navigationDrawerItems = listOf(
    NavigationDrawerItem(Icons.Default.Home, "Вакансии", NavigationGraph.MainApp.HOME_SCREEN),
    NavigationDrawerItem(Icons.Default.AccountCircle, "Профиль", NavigationGraph.MainApp.PROFILE),
    NavigationDrawerItem(Icons.Default.ChatBubble, "Предложения", NavigationGraph.MainApp.JOB_APPLICATIONS),
    NavigationDrawerItem(Icons.Default.Settings, "Настройки", NavigationGraph.MainApp.SETTINGS)
)
