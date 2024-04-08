package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.User
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.CompanyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.SkillDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.VacancyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.WorkExperienceDTO

fun UserDTO.UserDTO.toDomainUser() =
    User(
        email = email,
        username = username,
        role = role,
        userStatus = userStatus,
        imageUrl = imageUrl
    )

fun ResumeDTO.toDomainResume() =
    Resume(
        id = id,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        contactEmail = contactEmail,
        applyPosition = applyPosition,
        skills = skills?.map { it.toDomainSkill() }?.toMutableList() ?: mutableListOf(),
        publicationStatus = publicationStatus,
        workExperience = workExperience?.map { it.toDomainWorkExperience() }?.toMutableList() ?: mutableListOf()
    )

fun SkillDTO.toDomainSkill() =
    Skill(
        name = name,
        skillLevel = skillLevel
    )

fun WorkExperienceDTO.toDomainWorkExperience() =
    WorkExperience(
        company = company.toDomainCompany(),
        position = position,
        positionLevel = positionLevel,
        salary = salary,
        months = months
    )

fun CompanyDTO.toDomainCompany() =
    Company(
        name = name
    )

fun VacancyDTO.toDomainVacancy() =
    Vacancy(
        id = id,
        title = title,
        description = description,
        company = company,
        minSalary = minSalary,
        maxSalary = maxSalary,
        publicationStatus = publicationStatus
    )