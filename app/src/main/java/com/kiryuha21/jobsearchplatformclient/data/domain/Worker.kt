package com.kiryuha21.jobsearchplatformclient.data.domain

class Worker(
    id: Int,
    email: String,
    login: String,
    val resumes: List<Resume>? = null
) : BaseUser(id, email, login)
