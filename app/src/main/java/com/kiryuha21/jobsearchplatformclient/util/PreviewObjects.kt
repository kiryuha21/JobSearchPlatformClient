package com.kiryuha21.jobsearchplatformclient.util

import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.PositionLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.SkillLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience

object PreviewObjects {
    val previewResume1 = Resume(
        id = "12khe12nj1nek",
        firstName = "John",
        lastName = "Smit",
        birthDate = 0,
        phoneNumber = "89661081500",
        contactEmail = "hey@gmail.com",
        applyPosition = "Senior C++ developer",
        skills = listOf(
            Skill(
                "C++ development",
                SkillLevel.HasCommercialProjects
            ),
            Skill(
                "Drinking Tea",
                SkillLevel.AwareOf
            ),
            Skill(
                "Sleeping well",
                SkillLevel.AwareOf
            ),
            Skill(
                "Dropping prod",
                SkillLevel.AwareOf
            )
        ),
        workExperience = listOf(
            WorkExperience(
                Company(
                    "yandex",
                ),
                "C++ developer",
                PositionLevel.Lead,
                100500,
                420
            )
        ),
        publicationStatus = PublicationStatus.Published
    )

    val previewVacancy1 = Vacancy(
        id = "3",
        title = "Cave Digger",
        description = "In this good company you will have everything you want and even money",
        company = Company("Gold rocks"),
        minSalary = 15000,
        maxSalary = 20000,
        publicationStatus = PublicationStatus.Published,
        requiredSkills = listOf(
            Skill(name = "chilling", skillLevel = SkillLevel.HasCommercialProjects)
        ),
        requiredWorkExperience = listOf(
            WorkExperience(company = Company("yandex"), positionLevel = PositionLevel.Middle, position = "C++ dev", salary = 100000, months = 10)
        ),
        imageUrl = "https://fakeimg.pl/350x200/?text=World&font=lobster",
    )
}