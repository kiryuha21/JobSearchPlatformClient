package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kiryuha21.jobsearchplatformclient.ui.components.SignUpForm
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(authViewModel: AuthViewModel) {
    val state by authViewModel.state

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        ) {
            Text(text = "Регистрация")
        }
        SignUpForm(
            onRegister = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(authViewModel = viewModel())
}