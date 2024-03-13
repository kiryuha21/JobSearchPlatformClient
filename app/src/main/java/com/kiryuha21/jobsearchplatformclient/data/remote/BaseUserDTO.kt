package com.kiryuha21.jobsearchplatformclient.data.remote

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole

data class BaseUserDTO(
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("role")
    val role: UserRole
)