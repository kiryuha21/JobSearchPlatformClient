package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.ui.components.LoginForm
import com.kiryuha21.jobsearchplatformclient.ui.components.NotSignedUpHelper
import com.kiryuha21.jobsearchplatformclient.ui.components.ResetPasswordHelper
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel

@Composable
fun LogInScreen(authViewModel: AuthViewModel, navController: NavController) {
    val state by authViewModel.state

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        ) {
            Text(
                text = "Платформа для поиска работы",
            )
        }
        LoginForm(
            onLogin = {
                // TODO: here should be credential check
                navController.navigate(NavigationGraph.MainApp.route ) {
                    popUpTo(NavigationGraph.Authentication.route) {
                        inclusive = true
                    }
                }
            },
            onResetPassword = { navController.navigate(NavigationGraph.Authentication.ResetPassword) }
        )
        NotSignedUpHelper(
            onClick = { navController.navigate(NavigationGraph.Authentication.SignUp) },
            modifier = Modifier.fillMaxHeight()
        )
    }
}