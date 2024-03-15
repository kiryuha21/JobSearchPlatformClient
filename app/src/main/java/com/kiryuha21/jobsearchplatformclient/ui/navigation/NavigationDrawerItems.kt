package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import com.kiryuha21.jobsearchplatformclient.data.domain.NavigationDrawerItem

val navigationDrawerItems = listOf(
    NavigationDrawerItem(Icons.Filled.Home, "Вакансии", NavigationGraph.MainApp.HOME_SCREEN),
    NavigationDrawerItem(Icons.Filled.AccountCircle, "Профиль", NavigationGraph.MainApp.PROFILE),
    NavigationDrawerItem(Icons.Filled.Settings, "Настройки", NavigationGraph.MainApp.SETTINGS)
)