package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.PageRequestFilter
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.ResumeFilters
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.VacancyFilters
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.CompanyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.PageRequestFilterDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.ResumeFiltersDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.SkillDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.VacancyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.VacancyFiltersDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.WorkExperienceDTO

fun Resume.toResumeDTO() =
    ResumeDTO.ResumeRequestDTO(
        id = id,
        workerLogin = CurrentUser.info.username,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate ?: throw Exception("birth date should be chosen"),
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

fun Vacancy.toVacancyDTO() =
    VacancyDTO.VacancyRequestDTO(
        id = id,
        employerLogin = CurrentUser.info.username,
        title = title,
        description = description,
        company = company.toCompanyDTO(),
        minSalary = minSalary,
        maxSalary = maxSalary,
        publicationStatus = publicationStatus,
        requiredSkills = requiredSkills.map { it.toSkillDTO() },
        requiredWorkExperience = requiredWorkExperience.map { it.toWorkExperienceDTO() }
    )

fun PageRequestFilter.toPageRequestFilterDTO() =
    PageRequestFilterDTO(
        pageNumber = pageNumber,
        pageSize = pageSize,
        sortingDirection = sortingDirection,
        sortProperty = sortProperty
    )

fun VacancyFilters.toVacancyFiltersDTO() =
    VacancyFiltersDTO(
        placedAt = placedAt,
        query = query,
        minSalary = minSalary,
        maxSalary = maxSalary,
        requiredSkills = requiredSkills,
        requiredWorkExperience = requiredWorkExperience,
        pageRequestFilter = pageRequestFilter.toPageRequestFilterDTO()
    )

fun ResumeFilters.toResumeFiltersDTO() =
    ResumeFiltersDTO(
        placedAt = placedAt,
        youngerThan = youngerThan,
        olderThan = olderThan,
        isImageSet = isImageSet,
        applyPosition = applyPosition,
        skills = skills,
        workExperience = workExperience,
        pageRequestFilter = pageRequestFilter.toPageRequestFilterDTO()
    )