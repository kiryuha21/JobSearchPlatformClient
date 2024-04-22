package com.kiryuha21.jobsearchplatformclient.ui.navigation

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object CallbacksRegistry {
    var logoutCallback: MutableState<() -> Unit> = mutableStateOf({})
    var setProfileImageCallback: MutableState<(Bitmap) -> Unit> = mutableStateOf({})
}