package com.kiryuha21.jobsearchplatformclient.ui.navigation

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object CallbacksRegistry {
    var logoutCallback: () -> Unit = {}
    var setProfileImageCallback: (Bitmap) -> Unit = {}
}