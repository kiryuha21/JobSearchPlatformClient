package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience

data class VacancyFiltersDTO(
    @SerializedName("placedAt")
    val placedAt: Long?,
    @SerializedName("query")
    val query: String?,
    @SerializedName("minSalary")
    val minSalary: Int?,
    @SerializedName("maxSalary")
    val maxSalary: Int?,
    @SerializedName("requiredSkills")
    val requiredSkills: List<Skill>?,
    @SerializedName("requiredWorkExperience")
    val requiredWorkExperience: List<WorkExperience>?,
    @SerializedName("pageRequestFilter")
    val pageRequestFilter: PageRequestFilterDTO
)
