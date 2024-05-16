package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.JobApplicationDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface JobApplicationAPI {
    @POST("job-application")
    suspend fun createNewJobApplication(@Header("Authorization") authToken: String, @Body jobApplicationDTO: JobApplicationDTO)

    @GET("job-application/count")
    suspend fun getNewJobApplicationsCount(@Header("Authorization") authToken: String) : Int

    @GET("job-application/received")
    suspend fun getReceivedApplications(@Header("Authorization") authToken: String) : List<JobApplicationDTO>

    @GET("job-application/sent")
    suspend fun getSentApplications(@Header("Authorization") authToken: String) : List<JobApplicationDTO>

    @POST("job-application/mark/{applicationId}")
    suspend fun markSeen(
        @Header("Authorization") authToken: String,
        @Path("applicationId") applicationId: String
    )
}