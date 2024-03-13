package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.TokenDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenAPI {
    @POST("auth/create_token")
    suspend fun createToken(@Body credentials: TokenDTO.TokenCreateRequestDTO) : TokenDTO.TokenResponseDTO

    @POST("auth/refresh_token")
    suspend fun refreshToken(@Body refreshToken: TokenDTO.TokenRefreshRequestDTO) : TokenDTO.TokenRefreshResponseDTO

    @POST("auth/renew_refresh_token")
    suspend fun renewRefreshToken(@Body refreshToken: TokenDTO.TokenRefreshRequestDTO) : TokenDTO.TokenResponseDTO
}