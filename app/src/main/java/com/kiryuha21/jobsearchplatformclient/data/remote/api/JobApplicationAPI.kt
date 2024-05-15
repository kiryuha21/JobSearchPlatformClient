package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.JobApplicationDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface JobApplicationAPI {
    @GET("response/count")
    suspend fun getNewJobApplicationsCount(@Header("Authorization") authToken: String) : Int

    @GET("response")
    suspend fun getJobApplications(@Header("Authorization") authToken: String) : List<JobApplicationDTO>

    @POST("responses/mark/{applicationId}")
    suspend fun markSeen(
        @Header("Authorization") authToken: String,
        @Path("applicationId") applicationId: String
    )
}