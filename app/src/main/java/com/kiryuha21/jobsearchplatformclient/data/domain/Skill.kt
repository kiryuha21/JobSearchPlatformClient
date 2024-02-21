package com.kiryuha21.jobsearchplatformclient.data.domain

enum class SkillLevel {
    AwareOf,
    Tested,
    HasPetProjects,
    HasCommercialProjects
}

data class Skill(
    val name: String,
    val skillLevel: SkillLevel
)
