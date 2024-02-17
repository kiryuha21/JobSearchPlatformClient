package com.kiryuha21.jobsearchplatformclient.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationDrawerItem(
    val icon: ImageVector,
    val text: String
)

val navigationDrawerItems = listOf(
    NavigationDrawerItem(Icons.Filled.Home, "Вакансии"),
    NavigationDrawerItem(Icons.Filled.AccountCircle, "Аккаунт")
)
