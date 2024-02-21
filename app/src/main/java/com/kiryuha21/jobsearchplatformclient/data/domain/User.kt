package com.kiryuha21.jobsearchplatformclient.data.domain

data class User(
    val id: Int,
    val email: String,
    val login: String,
    val resumes: List<Resume>
)
