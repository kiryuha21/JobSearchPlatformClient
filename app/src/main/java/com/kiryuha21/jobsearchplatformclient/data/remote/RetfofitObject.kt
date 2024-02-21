package com.kiryuha21.jobsearchplatformclient.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val baseUrl = "http://10.0.2.2:8010/api/"

    // TODO: check if it's really singleton
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}