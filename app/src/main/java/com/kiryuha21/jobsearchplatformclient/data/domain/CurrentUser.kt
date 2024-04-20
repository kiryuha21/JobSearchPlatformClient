package com.kiryuha21.jobsearchplatformclient.data.domain

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainUser
import com.kiryuha21.jobsearchplatformclient.data.remote.AuthToken
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject
import com.kiryuha21.jobsearchplatformclient.data.remote.api.UserAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CurrentUser {
    private val userRetrofit by lazy { RetrofitObject.retrofit.create(UserAPI::class.java) }
    private val _info: MutableState<User> = mutableStateOf(User("", "", UserRole.Worker, UserStatus.Active, null))
    val info by _info

    suspend fun tryLogIn(login: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                AuthToken.createToken(login, password)
                _info.value = userRetrofit.getUserByUsername(login).toDomainUser()
                true
            } catch (e: Exception) {
                Log.d("tag1", e.toString())
                false
            }
        }
    }

    suspend fun trySignUp(user: UserDTO.SignUpUserDTO): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                _info.value = userRetrofit.createNewUser(user).toDomainUser()
                AuthToken.createToken(user.username, user.password)
                true
            } catch (e: Exception) {
                Log.d("tag1", e.toString())
                false
            }
        }
    }
}