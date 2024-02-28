package com.kiryuha21.jobsearchplatformclient.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitEntity {
    private const val baseUrl = "http://10.0.2.2:8080/data-api/"

    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}