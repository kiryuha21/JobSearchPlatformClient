package com.kiryuha21.jobsearchplatformclient.data.remote.api

import com.kiryuha21.jobsearchplatformclient.data.remote.dto.JobApplicationDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface JobApplicationAPI {
    @POST("job-application")
    suspend fun createNewJobApplication(@Header("Authorization") authToken: String, @Body jobApplicationDTO: JobApplicationDTO.JobApplicationRequestDTO)

    @GET("job-application/received/count_unseen")
    suspend fun countUnseenJobApplications(@Header("Authorization") authToken: String) : Int

    @GET("job-application/received/count_unnotified")
    suspend fun countUnnotifiedJobApplications(@Header("Authorization") authToken: String) : Int

    @GET("job-application/received")
    suspend fun getReceivedApplications(@Header("Authorization") authToken: String) : List<JobApplicationDTO.JobApplicationResponseDTO>

    @GET("job-application/sent")
    suspend fun getSentApplications(@Header("Authorization") authToken: String) : List<JobApplicationDTO.JobApplicationResponseDTO>

    @POST("job-application/mark/{applicationId}")
    suspend fun markSeen(
        @Header("Authorization") authToken: String,
        @Path("applicationId") applicationId: String
    )
}