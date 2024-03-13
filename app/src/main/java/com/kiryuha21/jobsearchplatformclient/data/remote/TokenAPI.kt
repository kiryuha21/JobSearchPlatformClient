package com.kiryuha21.jobsearchplatformclient.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface TokenAPI {
    @POST("auth/get_token")
    suspend fun getToken(@Body credentials: TokenDTO.TokenRequestDTO) : TokenDTO.TokenResponseDTO
}