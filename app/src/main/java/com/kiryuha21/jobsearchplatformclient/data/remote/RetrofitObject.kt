package com.kiryuha21.jobsearchplatformclient.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    private const val BASE_URL = "http://46.8.53.121:8080/api/"

    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}