package com.kiryuha21.jobsearchplatformclient.data.domain

data class Vacancy(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val company: Company = Company(""),
    val minSalary: Int = 0,
    val maxSalary: Int = 0,
    val publicationStatus: PublicationStatus = PublicationStatus.Published,
    val imageUrl: String? = null
)
