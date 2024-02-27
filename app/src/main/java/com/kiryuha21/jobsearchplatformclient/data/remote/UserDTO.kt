package com.kiryuha21.jobsearchplatformclient.data.remote

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume

data class UserDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("resumes")
    val resumes: List<Resume>
)