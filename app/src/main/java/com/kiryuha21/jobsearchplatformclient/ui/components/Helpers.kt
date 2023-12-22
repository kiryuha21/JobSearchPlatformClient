package com.kiryuha21.jobsearchplatformclient.ui.components

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun BackHandlerWithWarning(message: String, msDelay: Long) {
    val context = LocalContext.current
    var pressedOnce by remember { mutableStateOf(false) }

    BackHandler(enabled = !pressedOnce) {
        if (!pressedOnce) {
            pressedOnce = true
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ pressedOnce = false }, msDelay)
    }
}