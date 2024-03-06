package com.kiryuha21.jobsearchplatformclient.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface BaseUserAPI {
    @POST("base_users")
    suspend fun createNewUser(@Body user: BaseUserDTO) : BaseUserDTO
}