package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeFiltersDTO
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
import retrofit2.http.Query

interface ResumeAPI {
    @GET("resume/worker_username/{login}")
    suspend fun getAllResumesByWorkerLogin(
        @Header("Authorization") authToken: String,
        @Path("login") login: String
    ): List<ResumeDTO.ResumeResponseDTO>

    @GET("resume/worker_username/{login}")
    suspend fun getPublicResumesByWorkerLogin(@Path("login") login: String): List<ResumeDTO.ResumeResponseDTO>

    @GET("resume/{resumeId}")
    suspend fun getPrivateResumeById(
        @Header("Authorization") authToken: String,
        @Path("resumeId") resumeId: String
    ): ResumeDTO.ResumeResponseDTO

    @GET("resume/{resumeId}")
    suspend fun getPublicResumeById(@Path("resumeId") resumeId: String): ResumeDTO.ResumeResponseDTO

    @POST("resume/filter")
    suspend fun getFilteredResumes(@Body filters: ResumeFiltersDTO): List<ResumeDTO.ResumeResponseDTO>

    @GET("resume/recommendations")
    suspend fun getResumeRecommendations(
        @Header("Authorization") authToken: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int = 10
    ) : List<ResumeDTO.ResumeResponseDTO>

    @POST("resume")
    suspend fun createNewResume(
        @Header("Authorization") authToken: String,
        @Body resume: ResumeDTO.ResumeRequestDTO
    ): ResumeDTO.ResumeResponseDTO

    @Multipart
    @POST("resume/picture/{id}")
    suspend fun setPicture(
        @Header("Authorization") authToken: String,
        @Path("id") id: String,
        @Part picture: MultipartBody.Part
    )

    @PUT("resume/{id}")
    suspend fun editResume(
        @Header("Authorization") authToken: String,
        @Path("id") id: String,
        @Body resume: ResumeDTO.ResumeRequestDTO
    ): ResumeDTO.ResumeResponseDTO

    @DELETE("resume/{id}")
    suspend fun deleteResume(
        @Header("Authorization") authToken: String,
        @Path("id") id: String
    ): Response<Unit>
}
