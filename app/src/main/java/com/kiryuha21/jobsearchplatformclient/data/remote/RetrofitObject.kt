package com.kiryuha21.jobsearchplatformclient.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    private const val BASE_URL = "https://job-search-platform.ru/api/"

    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(UnsafeHttpClient.instance)  // TODO: replace with safe client
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}