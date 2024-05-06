package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.local.entity.VacancyEntity

fun VacancyEntity.toDomainVacancy() =
    Vacancy(
        id = id,
        title = title,
        description = description,
        company = company,
        minSalary = minSalary,
        maxSalary = maxSalary,
        requiredWorkExperience = requiredWorkExperience,
        requiredSkills = requiredSkills,
        publicationStatus = publicationStatus,
        imageUrl = imageUrl
    )