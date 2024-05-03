package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience

data class ResumeFiltersDTO(
    @SerializedName("placedAt")
    val placedAt: Long?,
    @SerializedName("youngerThan")
    val youngerThan: Int?,
    @SerializedName("olderThan")
    val olderThan: Int?,
    @SerializedName("isImageSet")
    val isImageSet: Boolean?,
    @SerializedName("applyPosition")
    val applyPosition: String?,
    @SerializedName("skills")
    val skills: List<Skill>?,
    @SerializedName("workExperience")
    val workExperience: List<WorkExperience>?,
    @SerializedName("pageRequestFilter")
    val pageRequestFilter: PageRequestFilterDTO
)
