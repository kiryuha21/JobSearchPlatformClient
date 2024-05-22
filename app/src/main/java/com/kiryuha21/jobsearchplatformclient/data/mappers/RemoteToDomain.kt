package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.JobApplication
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.User
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.CompanyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.JobApplicationDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.SkillDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
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

fun ResumeDTO.ResumeResponseDTO.toDomainResume() =
    Resume(
        id = id,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        phoneNumber = phoneNumber,
        contactEmail = contactEmail,
        applyPosition = applyPosition,
        skills = skills.map { it.toDomainSkill() },
        publicationStatus = publicationStatus,
        workExperience = workExperience.map { it.toDomainWorkExperience() },
        imageUrl = imageUrl
    )

fun ResumeDTO.ResumeRequestDTO.toDomainResume() =
    Resume(
        id = id,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        phoneNumber = phoneNumber,
        contactEmail = contactEmail,
        applyPosition = applyPosition,
        skills = skills.map { it.toDomainSkill() },
        publicationStatus = publicationStatus,
        workExperience = workExperience.map { it.toDomainWorkExperience() },
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
        salary = salary.toString(),
        months = months.toString()
    )

fun CompanyDTO.toDomainCompany() =
    Company(
        name = name
    )

fun VacancyDTO.VacancyResponseDTO.toDomainVacancy() =
    Vacancy(
        id = id,
        title = title,
        description = description,
        company = company.toDomainCompany(),
        minSalary = minSalary.toString(),
        maxSalary = maxSalary.toString(),
        publicationStatus = publicationStatus,
        requiredWorkExperience = requiredWorkExperience.map { it.toDomainWorkExperience() },
        requiredSkills = requiredSkills.map { it.toDomainSkill() },
        imageUrl = imageUrl
    )

fun VacancyDTO.VacancyRequestDTO.toDomainVacancy() =
    Vacancy(
        id = id,
        title = title,
        description = description,
        company = company.toDomainCompany(),
        minSalary = minSalary.toString(),
        maxSalary = maxSalary.toString(),
        publicationStatus = publicationStatus,
        requiredWorkExperience = requiredWorkExperience.map { it.toDomainWorkExperience() },
        requiredSkills = requiredSkills.map { it.toDomainSkill() },
    )

fun JobApplicationDTO.toDomainJobApplication() =
    JobApplication(
        id = id,
        senderUsername = senderUsername,
        referenceVacancyId = referenceVacancyId,
        referenceResumeId = referenceResumeId,
        message = message,
        seen = seen
    )