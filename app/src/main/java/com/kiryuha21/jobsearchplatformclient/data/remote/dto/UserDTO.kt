package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole

sealed class UserDTO {
    data class SignUpUserDTO(
        @SerializedName("email")
        val email: String,
        @SerializedName("username")
        val username: String,
        @SerializedName("password")
        val password: String,
        @SerializedName("role")
        val role: UserRole
    )

    data class UserDTO(
        @SerializedName("email")
        val email: String,
        @SerializedName("username")
        val username: String,
        @SerializedName("role")
        val role: UserRole,
        @SerializedName("imageUrl")
        val imageUrl: String?
    )
}