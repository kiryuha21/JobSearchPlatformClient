package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

interface ResumeAPI {
    @GET("resume/worker_username/{login}")
    suspend fun getResumesByWorkerLogin(@Path("login") login: String): List<ResumeDTO.ResumeResponseDTO>

    @GET("resume/{resumeId}")
    suspend fun getResumeById(@Path("resumeId") resumeId: String): ResumeDTO.ResumeResponseDTO

    @GET("resume")
    suspend fun getMatchingResumes(): List<ResumeDTO.ResumeResponseDTO>

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
