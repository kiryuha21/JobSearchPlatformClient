package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.SkillLevel

data class SkillDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("skillLevel")
    val skillLevel: SkillLevel
)
