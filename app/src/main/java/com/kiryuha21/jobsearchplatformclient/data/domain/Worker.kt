package com.kiryuha21.jobsearchplatformclient.data.domain

class Worker(
    email: String,
    login: String,
    val resumes: List<Resume>? = null
) : BaseUser(email, login)
