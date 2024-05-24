package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName

sealed class JobApplicationDTO {
    data class JobApplicationResponseDTO(
        @SerializedName("id")
        val id: String,
        @SerializedName("senderInitials")
        val senderInitials: String,
        @SerializedName("referenceResumeId")
        val referenceResumeId: String,
        @SerializedName("referenceVacancyId")
        val referenceVacancyId: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("seen")
        val seen: Boolean
    )

    data class JobApplicationRequestDTO(
        @SerializedName("referenceResumeId")
        val referenceResumeId: String,
        @SerializedName("referenceVacancyId")
        val referenceVacancyId: String,
        @SerializedName("message")
        val message: String,
    )
}

