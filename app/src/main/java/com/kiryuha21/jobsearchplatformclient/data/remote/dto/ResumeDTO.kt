package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus

sealed class ResumeDTO {
    data class ResumeRequestDTO(
        @SerializedName("id")
        val id: String,
        @SerializedName("firstName")
        val firstName: String,
        @SerializedName("lastName")
        val lastName: String,
        @SerializedName("birthDate")
        val birthDate: Long,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("contactEmail")
        val contactEmail: String,
        @SerializedName("applyPosition")
        val applyPosition: String,
        @SerializedName("skills")
        val skills: List<SkillDTO> = emptyList(),
        @SerializedName("workExperience")
        val workExperience: List<WorkExperienceDTO> = emptyList(),
        @SerializedName("status")
        val publicationStatus: PublicationStatus
    )

    data class ResumeResponseDTO(
        @SerializedName("id")
        val id: String,
        @SerializedName("workerUsername")
        val workerUsername: String,
        @SerializedName("firstName")
        val firstName: String,
        @SerializedName("lastName")
        val lastName: String,
        @SerializedName("birthDate")
        val birthDate: Long,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("contactEmail")
        val contactEmail: String,
        @SerializedName("applyPosition")
        val applyPosition: String,
        @SerializedName("skills")
        val skills: List<SkillDTO> = emptyList(),
        @SerializedName("workExperience")
        val workExperience: List<WorkExperienceDTO> = emptyList(),
        @SerializedName("status")
        val publicationStatus: PublicationStatus,
        @SerializedName("imageUrl")
        val imageUrl: String?
    )
}


