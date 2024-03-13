package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.BaseUserDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BaseUserAPI {
    @POST("base_users")
    suspend fun createNewUser(@Body user: BaseUserDTO) : BaseUserDTO

    @GET("base_users/{username}")
    suspend fun getUserByUsername(@Path("username") username: String) : BaseUserDTO
}