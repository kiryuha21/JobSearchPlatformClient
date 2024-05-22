package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.local.entity.ResumeEntity
import com.kiryuha21.jobsearchplatformclient.data.local.entity.VacancyEntity
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
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
        minSalary = minSalary.toString(),
        maxSalary = maxSalary.toString(),
        requiredWorkExperience = requiredWorkExperience.map { it.toDomainWorkExperience() },
        requiredSkills = requiredSkills.map { it.toDomainSkill() },
        publicationStatus = publicationStatus,
        imageUrl = imageUrl
    )

fun ResumeDTO.ResumeResponseDTO.toResumeEntity() =
    ResumeEntity(
        id = id,
        username = CurrentUser.info.username,
        unixSeconds = Instant.now().epochSecond,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        phoneNumber = phoneNumber,
        contactEmail = contactEmail,
        applyPosition = applyPosition,
        skills = skills.map { it.toDomainSkill() },
        workExperience = workExperience.map { it.toDomainWorkExperience() },
        publicationStatus = publicationStatus,
        imageUrl = imageUrl
    )