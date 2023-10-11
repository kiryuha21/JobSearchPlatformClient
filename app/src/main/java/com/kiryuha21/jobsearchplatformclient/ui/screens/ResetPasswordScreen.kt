package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel

@Composable
fun ResetPasswordScreen(authViewModel: AuthViewModel) {
    val state by authViewModel.state

    Text(text = "reset password screen")
}