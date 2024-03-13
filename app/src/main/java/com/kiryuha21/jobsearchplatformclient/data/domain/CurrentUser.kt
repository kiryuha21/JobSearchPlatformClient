package com.kiryuha21.jobsearchplatformclient.data.domain

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kiryuha21.jobsearchplatformclient.data.mappers.toBaseUserDTO
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainUser
import com.kiryuha21.jobsearchplatformclient.data.remote.AuthToken
import com.kiryuha21.jobsearchplatformclient.data.remote.api.BaseUserAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CurrentUser {
    private val userRetrofit = RetrofitObject.retrofit.create(BaseUserAPI::class.java)
    var userInfo: MutableState<BaseUser> = mutableStateOf(BaseUser("", "", "", UserRole.Worker))
        private set

    suspend fun tryLogIn(login: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                AuthToken.createToken(login, password)
                userInfo.value = userRetrofit.getUserByUsername(login).toDomainUser()
                true
            } catch (e: Exception) {
                Log.d("tag1", e.toString())
                false
            }
        }
    }

    suspend fun trySignUp(user: BaseUser): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                userInfo.value = userRetrofit.createNewUser(user.toBaseUserDTO()).toDomainUser()
                AuthToken.createToken(user.username, user.password)
                true
            } catch (e: Exception) {
                Log.d("tag1", e.toString())
                false
            }
        }
    }
}