package com.kiryuha21.jobsearchplatformclient.data.domain

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainUser
import com.kiryuha21.jobsearchplatformclient.data.remote.AuthToken
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject.userRetrofit
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CurrentUser {
    private val defaultUserState = User("", "", UserRole.Undefined, UserStatus.Undefined, null)
    private val _info = mutableStateOf(defaultUserState)
    val info by _info
    private val _selectedImageUri = mutableStateOf<Uri?>(null)
    val selectedImageUri by _selectedImageUri

    suspend fun tryLogIn(login: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            networkCallWrapper(
                networkCall = {
                    AuthToken.createToken(login, password)
                    loadUserInfo(login)
                }
            )
        }
    }

    suspend fun trySignUp(user: UserDTO.SignUpUserDTO): Boolean {
        return withContext(Dispatchers.IO) {
            networkCallWrapper(
                networkCall = {
                    _info.value = userRetrofit.createNewUser(user).toDomainUser()
                    AuthToken.createToken(user.username, user.password)
                }
            )
        }
    }

    // IMPORTANT: Do not wrap in try catch wrapper
    //  this should be done on higher level
    suspend fun loadUserInfo(username: String) {
        withContext(Dispatchers.IO) {
            _info.value = userRetrofit.getUserByUsername(username).toDomainUser()
            if (info.imageUrl != null) {
                _selectedImageUri.value = Uri.parse(info.imageUrl)
            }
        }
    }

    fun updateUserFromDTO(userDTO: UserDTO.UserDTO) {
        _info.value = userDTO.toDomainUser()
    }

    fun setImageUri(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun logOut() {
        _info.value = defaultUserState
        _selectedImageUri.value = null
    }
}