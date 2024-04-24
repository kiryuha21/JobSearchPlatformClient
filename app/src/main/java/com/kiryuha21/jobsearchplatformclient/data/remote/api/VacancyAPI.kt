package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.VacancyDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface VacancyAPI {
    @GET("vacancy/employer_username/{login}")
    suspend fun getVacanciesByEmployerLogin(@Path("login") login: String): List<VacancyDTO.VacancyResponseDTO>

    @GET("vacancy/{vacancyId}")
    suspend fun getVacancyById(@Path("vacancyId") vacancyId: String): VacancyDTO.VacancyResponseDTO

    @GET("vacancy")
    suspend fun getMatchingVacancies(): List<VacancyDTO.VacancyResponseDTO>

    @POST("vacancy")
    suspend fun createNewVacancy(
        @Header("Authorization") authToken: String,
        @Body vacancy: VacancyDTO.VacancyRequestDTO
    ): VacancyDTO.VacancyResponseDTO

    @Multipart
    @POST("vacancy/picture/{id}")
    suspend fun setPicture(
        @Header("Authorization") authToken: String,
        @Path("id") id: String,
        @Part picture: MultipartBody.Part
    )

    @PUT("vacancy/{id}")
    suspend fun editVacancy(
        @Header("Authorization") authToken: String,
        @Path("id") id: String,
        @Body vacancy: VacancyDTO.VacancyRequestDTO
    ): VacancyDTO.VacancyResponseDTO

    @DELETE("vacancy/{id}")
    suspend fun deleteVacancy(
        @Header("Authorization") authToken: String,
        @Path("id") id: String
    ): Response<Unit>
}