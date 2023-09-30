package com.kiryuha21.jobsearchplatformclient.api

import com.kiryuha21.jobsearchplatformclient.model.User

import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET("User")
    suspend fun getUsers() : Response<List<User>>
}