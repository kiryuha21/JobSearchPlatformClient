package com.kiryuha21.jobsearchplatformclient.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @GET("users")
    suspend fun getUsers() : Call<List<UserDTO>>

    @POST("users")
    suspend fun createNewUser(@Body user: UserDTO) : Call<UserDTO>
}