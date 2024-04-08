package com.kiryuha21.jobsearchplatformclient.data.domain

enum class SkillLevel {
    AwareOf,
    Tested,
    HasPetProjects,
    HasCommercialProjects
}

data class Skill(
    var name: String,
    var skillLevel: SkillLevel
) {
    override fun toString(): String =
        when (skillLevel) {
            SkillLevel.AwareOf -> "Ознакомлен с навыком/технологией"
            SkillLevel.Tested -> "Имел опыт с навыком/технологией"
            SkillLevel.HasPetProjects -> "Имеет pet-проекты с навыком/технологией"
            SkillLevel.HasCommercialProjects -> "Имеет коммерческий опыт с навыком/технологией"
        } + " \"$name\""
}
