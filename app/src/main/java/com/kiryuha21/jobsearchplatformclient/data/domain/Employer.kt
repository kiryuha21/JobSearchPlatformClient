package com.kiryuha21.jobsearchplatformclient.data.domain

class Employer(
    email: String,
    login: String,
    val vacancies: List<Vacancy>? = null
) : BaseUser(email, login)
