package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.BaseUser
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.BaseUserDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.CompanyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.SkillDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.VacancyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.WorkExperienceDTO

fun BaseUserDTO.toDomainUser() =
    BaseUser(
        email = this.email,
        username = this.username,
        password = this.password,
        role = this.role
    )

fun ResumeDTO.toDomainResume() =
    Resume(
        id = id,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        contactEmail = contactEmail,
        applyPosition = applyPosition,
        skills = skills?.map { it.toDomainSkill() } ?: emptyList(),
        workExperience = workExperience?.map { it.toDomainWorkExperience() } ?: emptyList()
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
        name = name,
        size = size
    )

fun VacancyDTO.toDomainVacancy() =
    Vacancy(
        id = id,
        title = title,
        description = description,
        company = company,
        minSalary = minSalary,
        maxSalary = maxSalary
    )