package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.CompanyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.SkillDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.WorkExperienceDTO

fun Resume.toResumeDTO() =
    ResumeDTO(
        id = id,
        workerLogin = CurrentUser.info.value.username,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        contactEmail = contactEmail,
        applyPosition = applyPosition,
        skills = skills.map { it.toSkillDTO() },
        workExperience = workExperience.map { it.toWorkExperienceDTO() },
        publicationStatus = publicationStatus
    )

fun Skill.toSkillDTO() =
    SkillDTO(
        name = name,
        skillLevel = skillLevel
    )

fun WorkExperience.toWorkExperienceDTO() =
    WorkExperienceDTO(
        company = company.toCompanyDTO(),
        position = position,
        positionLevel = positionLevel,
        salary = salary,
        months = months
    )

fun Company.toCompanyDTO() =
    CompanyDTO(
        name = name
    )