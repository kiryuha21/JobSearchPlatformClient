package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ResumeAPI {
    @GET("resume/worker_username/{login}")
    suspend fun getResumesByWorkerLogin(@Path("login") login: String): List<ResumeDTO>

    @GET("resume/{resumeId}")
    suspend fun getResumeById(@Path("resumeId") resumeId: String): ResumeDTO

    @GET("resume")
    suspend fun getMatchingResumes(): List<ResumeDTO>
}
