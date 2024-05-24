package com.kiryuha21.jobsearchplatformclient.data.domain

import kotlinx.serialization.Serializable

@Serializable
data class Skill(
    val name: String,
    val skillLevel: SkillLevel
) {
    fun isValid() =
        name.isNotBlank()

    override fun toString(): String =
        when (skillLevel) {
            SkillLevel.AwareOf -> "Ознакомлен с навыком/технологией"
            SkillLevel.Tested -> "Имел опыт с навыком/технологией"
            SkillLevel.HasPetProjects -> "Имеет pet-проекты с навыком/технологией"
            SkillLevel.HasCommercialProjects -> "Имеет коммерческий опыт с навыком/технологией"
        } + " \"$name\""

    fun asRequirement(): String =
        when (skillLevel) {
            SkillLevel.AwareOf -> "Быть ознакомленным с навыком/технологией"
            SkillLevel.Tested -> "Иметь опыт с навыком/технологией"
            SkillLevel.HasPetProjects -> "Иметь pet-проекты с навыком/технологией"
            SkillLevel.HasCommercialProjects -> "Иметь коммерческий опыт с навыком/технологией"
        } + " \"$name\""
}
