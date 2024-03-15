package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.VacancyDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface VacancyAPI {
    @GET("vacancies/employer_login/{login}")
    suspend fun getVacanciesByEmployerLogin(@Path("login") login: String): List<VacancyDTO>

    @GET("vacancies/{vacancyId}")
    suspend fun getVacancyById(@Path("vacancyId") vacancyId: String): VacancyDTO
}