package com.kiryuha21.jobsearchplatformclient.data.domain

import com.kiryuha21.jobsearchplatformclient.util.isNumeric

data class Vacancy(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val company: Company = Company(""),
    val minSalary: String = "",
    val maxSalary: String = "",
    val requiredWorkExperience: List<WorkExperience> = emptyList(),
    val requiredSkills: List<Skill> = emptyList(),
    val publicationStatus: PublicationStatus = PublicationStatus.Published,
    val imageUrl: String? = null
) {
    fun isValid() =
        title.isNotBlank() &&
        company.name.isNotBlank() &&
        minSalary.isNotBlank() && minSalary.isNumeric() &&
        maxSalary.isNotBlank() && maxSalary.isNumeric()

}
