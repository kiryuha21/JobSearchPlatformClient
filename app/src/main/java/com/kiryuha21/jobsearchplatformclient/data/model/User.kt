package com.kiryuha21.jobsearchplatformclient.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("resumes")
    val resumes: List<Resume>
)