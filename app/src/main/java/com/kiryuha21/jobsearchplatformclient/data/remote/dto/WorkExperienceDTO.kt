package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.PositionLevel

data class WorkExperienceDTO(
    @SerializedName("company")
    val company: CompanyDTO,
    @SerializedName("position")
    val position: String,
    @SerializedName("positionLevel")
    val positionLevel: PositionLevel,
    @SerializedName("salary")
    val salary: Int,
    @SerializedName("months")
    val months: Int
)
