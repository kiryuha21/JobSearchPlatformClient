package com.kiryuha21.jobsearchplatformclient.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @GET("users")
    suspend fun getUsers() : List<UserDTO>

    @POST("users")
    suspend fun createNewUser(@Body user: UserDTO) : UserDTO
}