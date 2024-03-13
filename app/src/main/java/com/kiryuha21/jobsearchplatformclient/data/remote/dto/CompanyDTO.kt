package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.CompanySize

data class CompanyDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("size")
    val size: CompanySize
)
