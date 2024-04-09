package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserAPI {
    @POST("user")
    suspend fun createNewUser(@Body user: UserDTO.SignUpUserDTO): UserDTO.UserDTO

    @GET("user/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): UserDTO.UserDTO

    @Multipart
    @POST("user/picture/{username}")
    suspend fun setPicture(
        @Header("Authorization") authToken: String,
        @Path("username") username: String,
        @Part picture: MultipartBody.Part
    )
}