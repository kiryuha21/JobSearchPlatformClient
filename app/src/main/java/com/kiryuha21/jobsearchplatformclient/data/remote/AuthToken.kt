package com.kiryuha21.jobsearchplatformclient.data.remote

import android.util.Log
import com.auth0.android.jwt.JWT
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.mappers.toTokenRequestDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AuthToken {
    private val retrofit = RetrofitObject.retrofit.create(TokenAPI::class.java)
    private var token: JWT? = null

    suspend fun getToken() =
        when {
            token == null -> throw Exception("token should be created first!")
            token!!.isExpired(0) -> refreshToken()
            else -> token
        }

    suspend fun createToken(username: String, password: String) {
        withContext(Dispatchers.IO) {
            val tokenRequest = TokenDTO.TokenRequestDTO(username, password)
            val tokenDTO = retrofit.getToken(tokenRequest)
            token = JWT(tokenDTO.token)
        }
    }

    private suspend fun refreshToken() =
        withContext(Dispatchers.IO) {
            val tokenDTO = retrofit.getToken(CurrentUser.userInfo.value.toTokenRequestDTO())
            JWT(tokenDTO.token)
        }
}