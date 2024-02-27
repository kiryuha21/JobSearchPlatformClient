package com.kiryuha21.jobsearchplatformclient.data.domain

class Employer(
    id: Int,
    email: String,
    login: String,
    val vacancies: List<Vacancy>? = null
) : BaseUser(id, email, login)
