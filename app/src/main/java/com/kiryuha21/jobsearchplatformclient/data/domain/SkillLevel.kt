package com.kiryuha21.jobsearchplatformclient.data.domain

enum class SkillLevel {
    AwareOf,
    Tested,
    HasPetProjects,
    HasCommercialProjects
}

val descriptionToSkillLevel = mapOf(
    "Имею представление" to SkillLevel.AwareOf,
    "Имел дело" to SkillLevel.Tested,
    "Есть пет-проекты" to SkillLevel.HasPetProjects,
    "Есть коммерческие проекты" to SkillLevel.HasCommercialProjects
)