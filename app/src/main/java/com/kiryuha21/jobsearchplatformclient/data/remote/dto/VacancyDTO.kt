package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.Company

data class VacancyDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("company")
    val company: Company,
    @SerializedName("minSalary")
    val minSalary: Int,
    @SerializedName("maxSalary")
    val maxSalary: Int
)
