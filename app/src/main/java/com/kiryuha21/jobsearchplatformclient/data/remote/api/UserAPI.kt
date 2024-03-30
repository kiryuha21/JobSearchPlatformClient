package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAPI {
    @POST("user")
    suspend fun createNewUser(@Body user: UserDTO.SignUpUserDTO) : UserDTO.UserDTO

    @GET("user/{username}")
    suspend fun getUserByUsername(@Path("username") username: String) : UserDTO.UserDTO
}