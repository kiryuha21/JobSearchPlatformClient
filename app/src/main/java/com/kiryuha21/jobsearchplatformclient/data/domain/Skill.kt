package com.kiryuha21.jobsearchplatformclient.data.domain

import kotlinx.serialization.Serializable

enum class SkillLevel {
    AwareOf,
    Tested,
    HasPetProjects,
    HasCommercialProjects
}

@Serializable
data class Skill(
    val name: String,
    val skillLevel: SkillLevel
) {
    override fun toString(): String =
        when (skillLevel) {
            SkillLevel.AwareOf -> "Ознакомлен с навыком/технологией"
            SkillLevel.Tested -> "Имел опыт с навыком/технологией"
            SkillLevel.HasPetProjects -> "Имеет pet-проекты с навыком/технологией"
            SkillLevel.HasCommercialProjects -> "Имеет коммерческий опыт с навыком/технологией"
        } + " \"$name\""
}
