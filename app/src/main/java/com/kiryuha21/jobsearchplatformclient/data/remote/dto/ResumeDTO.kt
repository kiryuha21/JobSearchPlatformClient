package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ResumeDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("workerLogin")
    val workerLogin: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("contactEmail")
    val contactEmail: String,
    @SerializedName("applyPosition")
    val applyPosition: String,
    @SerializedName("skills")
    val skills: List<SkillDTO>? = emptyList(),
    @SerializedName("workExperience")
    val workExperience: List<WorkExperienceDTO>? = emptyList()
)
