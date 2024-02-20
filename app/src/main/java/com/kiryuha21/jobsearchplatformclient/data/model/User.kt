package com.kiryuha21.jobsearchplatformclient.data.model

object User {
    var userInfo: UserDTO? = null
        private set

    suspend fun tryLogIn(login: String, password: String): Boolean {
        // TODO: make real api call
        userInfo = UserDTO(
            id = 1,
            "test",
            "roflanchik",
            listOf(Resume(
                "John",
                "Smit",
                "12909483",
                "hey@gmail.com",
                listOf(Skill(
                    "c++ development",
                    SkillLevel.HasCommercialProjects
                )),
                listOf(WorkExperience(
                    Company(
                        "yandex",
                        CompanySize.Big
                    ),
                    "c++ developer",
                    PositionLevel.Lead,
                    100500,
                    420
                ))
            ))
        )

        return true
    }
}