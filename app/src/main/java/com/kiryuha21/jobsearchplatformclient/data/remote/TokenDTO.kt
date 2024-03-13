package com.kiryuha21.jobsearchplatformclient.data.remote

import com.google.gson.annotations.SerializedName

sealed class TokenDTO {
    data class TokenRequestDTO(
        @SerializedName("username")
        val username: String,
        @SerializedName("password")
        val password: String
    )

    data class TokenResponseDTO(
        @SerializedName("token")
        val token: String
    )
}

