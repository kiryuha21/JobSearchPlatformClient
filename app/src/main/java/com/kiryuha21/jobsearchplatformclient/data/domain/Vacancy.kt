package com.kiryuha21.jobsearchplatformclient.data.domain

data class Vacancy(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val company: Company = Company(""),
    val minSalary: Int = 0,
    val maxSalary: Int = 0,
    val requiredWorkExperience: List<WorkExperience> = emptyList(),
    val requiredSkills: List<Skill> = emptyList(),
    val publicationStatus: PublicationStatus = PublicationStatus.Published,
    val imageUrl: String? = null
)
