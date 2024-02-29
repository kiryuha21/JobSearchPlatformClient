package com.kiryuha21.jobsearchplatformclient.data.remote

import com.google.gson.annotations.SerializedName

data class Resume(
    @SerializedName("id")
    val id: Int,
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
    val skills: List<SkillDTO> = emptyList(),
    @SerializedName("workExperience")
    val workExperience: List<WorkExperienceDTO> = emptyList()
)
