package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.VacancyDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface VacancyAPI {
    @GET("vacancy/employer_username/{login}")
    suspend fun getVacanciesByEmployerLogin(@Path("login") login: String): List<VacancyDTO>

    @GET("vacancy/{vacancyId}")
    suspend fun getVacancyById(@Path("vacancyId") vacancyId: String): VacancyDTO

    @GET("vacancy")
    suspend fun getMatchingVacancies(): List<VacancyDTO>
}