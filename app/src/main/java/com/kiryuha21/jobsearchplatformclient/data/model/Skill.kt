package com.kiryuha21.jobsearchplatformclient.data.model

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
