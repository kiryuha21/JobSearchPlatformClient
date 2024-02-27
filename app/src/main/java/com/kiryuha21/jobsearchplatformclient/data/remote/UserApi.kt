package com.kiryuha21.jobsearchplatformclient.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET("User")
    suspend fun getUsers() : Response<List<UserDTO>>
}