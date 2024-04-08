package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CompanyDTO(
    @SerializedName("name")
    val name: String
)
