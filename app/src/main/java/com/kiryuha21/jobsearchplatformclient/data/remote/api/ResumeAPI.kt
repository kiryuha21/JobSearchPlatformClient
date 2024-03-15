package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ResumeAPI {
    @GET("resumes/worker_login/{login}")
    suspend fun getResumesByWorkerLogin(@Path("login") login: String): List<ResumeDTO>

    @GET("resumes/{resumeId}")
    suspend fun getResumeById(@Path("resumeId") resumeId: String): ResumeDTO
}
