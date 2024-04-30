package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus

sealed class VacancyDTO {
    data class VacancyRequestDTO(
        @SerializedName("id")
        val id: String,
        @SerializedName("employerUsername")
        val employerLogin: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("company")
        val company: CompanyDTO,
        @SerializedName("minSalary")
        val minSalary: Int,
        @SerializedName("maxSalary")
        val maxSalary: Int,
        @SerializedName("requiredWorkExperience")
        val requiredWorkExperience: List<WorkExperienceDTO> = emptyList(),
        @SerializedName("requiredSkills")
        val requiredSkills: List<SkillDTO> = emptyList(),
        @SerializedName("status")
        val publicationStatus: PublicationStatus
    )

    data class VacancyResponseDTO(
        @SerializedName("id")
        val id: String,
        @SerializedName("employerUsername")
        val employerLogin: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("company")
        val company: CompanyDTO,
        @SerializedName("minSalary")
        val minSalary: Int,
        @SerializedName("maxSalary")
        val maxSalary: Int,
        @SerializedName("requiredWorkExperience")
        val requiredWorkExperience: List<WorkExperienceDTO> = emptyList(),
        @SerializedName("requiredSkills")
        val requiredSkills: List<SkillDTO> = emptyList(),
        @SerializedName("status")
        val publicationStatus: PublicationStatus,
        @SerializedName("imageUrl")
        val imageUrl: String?
    )
}

