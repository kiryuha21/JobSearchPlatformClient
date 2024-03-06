package com.kiryuha21.jobsearchplatformclient.data.domain

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kiryuha21.jobsearchplatformclient.data.mappers.toBaseUserDTO
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainUser
import com.kiryuha21.jobsearchplatformclient.data.remote.BaseUserAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CurrentUser {
    private val userRetrofit = RetrofitEntity.retrofit.create(BaseUserAPI::class.java)
    var userInfo: MutableState<BaseUser> = mutableStateOf(BaseUser("", "", "", UserRole.Worker))
        private set

    suspend fun tryLogIn(login: String, password: String): Boolean {
        // TODO: make real api call
        userInfo.value = BaseUser(
            "test",
            "roflanchik",
            "secret",
            UserRole.Worker
        )

        return true
    }

    suspend fun trySignUp(user: BaseUser): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                userInfo.value = userRetrofit.createNewUser(user.toBaseUserDTO()).toDomainUser()
                true
            } catch (e: Exception) {
                Log.d("tag1", e.toString())
                false
            }
        }
    }
}