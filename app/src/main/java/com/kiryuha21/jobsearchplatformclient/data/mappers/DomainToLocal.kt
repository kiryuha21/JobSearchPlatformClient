package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.local.entity.VacancyEntity
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import java.time.Instant

fun Vacancy.toVacancyEntity() =
    VacancyEntity(
        id = id,
        username = CurrentUser.info.username,
        unixSeconds = Instant.now().epochSecond,
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