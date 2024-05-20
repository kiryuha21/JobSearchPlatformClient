package com.kiryuha21.jobsearchplatformclient.di

import com.kiryuha21.jobsearchplatformclient.data.remote.api.JobApplicationAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.api.ResumeAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.api.UserAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.api.VacancyAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    const val BASE_URL = "https://job-search-platform.ru"
    private const val API_URL = "${BASE_URL}/api/"

    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val userRetrofit: UserAPI by lazy { retrofit.create(UserAPI::class.java) }
    val resumeRetrofit: ResumeAPI by lazy { retrofit.create(ResumeAPI::class.java) }
    val vacancyRetrofit: VacancyAPI by lazy { retrofit.create(VacancyAPI::class.java) }
    val jobApplicationRetrofit: JobApplicationAPI by lazy { retrofit.create(JobApplicationAPI::class.java) }
}