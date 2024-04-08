package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus

data class VacancyDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("employerLogin")
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
    @SerializedName("status")
    val publicationStatus: PublicationStatus,
    @SerializedName("imageUrl")
    val imageUrl: String?
)
