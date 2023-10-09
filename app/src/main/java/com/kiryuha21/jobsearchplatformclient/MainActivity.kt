package com.kiryuha21.jobsearchplatformclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kiryuha21.jobsearchplatformclient.data.api.RetrofitHelper
import com.kiryuha21.jobsearchplatformclient.data.api.UserApi
import com.kiryuha21.jobsearchplatformclient.data.model.User
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationController
import com.kiryuha21.jobsearchplatformclient.ui.theme.JobSearchPlatformClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

@Composable
fun Users() {
    var users by remember { mutableStateOf(emptyList<User>()) }
    val retrofit = RetrofitHelper.getInstance().create(UserApi::class.java)
    LaunchedEffect(users) {
        try {
            users = retrofit.getUsers().body() ?: emptyList()
        } catch (_: Exception) {

        }
    }
    LazyColumn {
        itemsIndexed(
            users
        ) { _: Int, user: User ->
            Text(text = user.name)
        }
    }
}
