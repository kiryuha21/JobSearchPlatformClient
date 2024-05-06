package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.local.entity.VacancyEntity
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.VacancyDTO
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import java.time.Instant

fun VacancyDTO.VacancyResponseDTO.toVacancyEntity() =
    VacancyEntity(
        id = id,
        username = CurrentUser.info.username,
        unixSeconds = Instant.now().epochSecond,
        title = title,
        description = description,
        company = company.toDomainCompany(),
        minSalary = minSalary,
        maxSalary = maxSalary,
        requiredWorkExperience = requiredWorkExperience.map { it.toDomainWorkExperience() },
        requiredSkills = requiredSkills.map { it.toDomainSkill() },
        publicationStatus = publicationStatus,
        imageUrl = imageUrl
    )