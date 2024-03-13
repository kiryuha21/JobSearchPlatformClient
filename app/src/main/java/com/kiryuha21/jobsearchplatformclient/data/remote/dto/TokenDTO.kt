package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName

sealed class TokenDTO {
    data class TokenCreateRequestDTO(
        @SerializedName("username")
        val username: String,
        @SerializedName("password")
        val password: String
    )

    data class TokenRefreshRequestDTO(
        @SerializedName("refreshToken")
        val refreshToken: String
    )

    data class TokenRefreshResponseDTO(
        @SerializedName("token")
        val token: String
    )

    data class TokenResponseDTO(
        @SerializedName("token")
        val token: String,
        @SerializedName("refreshToken")
        val refreshToken: String
    )
}

