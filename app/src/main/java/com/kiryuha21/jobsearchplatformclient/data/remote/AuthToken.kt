package com.kiryuha21.jobsearchplatformclient.data.remote

import com.auth0.android.jwt.JWT
import com.kiryuha21.jobsearchplatformclient.data.remote.api.TokenAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.TokenDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// IMPORTANT: Do not wrap any functions in try catch wrapper
//  this should be done on higher level
object AuthToken {
    private val retrofit by lazy { RetrofitObject.retrofit.create(TokenAPI::class.java) }
    private var token: JWT? = null
    private var refreshToken: JWT? = null

    suspend fun getToken() =
        when {
            token == null -> throw Exception("token should be created first!")
            token!!.isExpired(0) -> refreshToken()
            else -> token
        }

    fun getRefreshToken() = refreshToken?.toString()

    suspend fun createToken(username: String, password: String) {
        withContext(Dispatchers.IO) {
            val tokenDTO = retrofit.createToken(TokenDTO.TokenCreateRequestDTO(username, password))
            token = JWT(tokenDTO.token)
            refreshToken = JWT(tokenDTO.refreshToken)
        }
    }

    suspend fun getLoginFromStoredToken(token: String) : String {
        val jwtToken = JWT(token)
        if (jwtToken.isExpired(0)) {
            throw Exception("refresh token is expired")
        }

        refreshToken = jwtToken
        refreshToken()

        val tokenValue = this.token ?: throw Exception("token is missing")
        return tokenValue.getClaim("sub").asString() ?: throw Exception("sub should present in token")
    }

    private suspend fun refreshToken(): JWT? {
        val refreshTokenValue = refreshToken ?: throw Exception("can't refresh without refresh token")
        val refreshTokenString = refreshTokenValue.toString()

        withContext(Dispatchers.IO) {
            if (refreshTokenValue.isExpired(5 * 60 * 60 * 24)) {  // in 5 days or less
                val tokenDTO = retrofit.renewRefreshToken(TokenDTO.TokenRefreshRequestDTO(refreshTokenString))
                token = JWT(tokenDTO.token)
                refreshToken = JWT(tokenDTO.refreshToken)
            } else {
                val tokenDTO = retrofit.refreshToken(TokenDTO.TokenRefreshRequestDTO(refreshTokenString))
                token = JWT(tokenDTO.token)
            }
        }

        return token
    }
}