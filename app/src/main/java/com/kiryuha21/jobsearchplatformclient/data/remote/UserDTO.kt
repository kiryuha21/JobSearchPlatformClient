package com.kiryuha21.jobsearchplatformclient.data.remote

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("email")
    val email: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("pass")
    val password: String
)