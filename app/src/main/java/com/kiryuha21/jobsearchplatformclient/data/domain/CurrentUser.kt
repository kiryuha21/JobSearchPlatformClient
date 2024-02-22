package com.kiryuha21.jobsearchplatformclient.data.domain

object CurrentUser {
    var userInfo: User = User(-1, "", "", emptyList())
        private set

    suspend fun tryLogIn(login: String, password: String): Boolean {
        // TODO: make real api call
        userInfo = User(
            1,
            "test",
            "roflanchik",
            listOf(
                Resume(
                    "John",
                    "Smit",
                    "12909483",
                    "hey@gmail.com",
                    listOf(
                        Skill(
                            "c++ development",
                            SkillLevel.HasCommercialProjects
                        )
                    ),
                    listOf(
                        WorkExperience(
                            Company(
                                "yandex",
                                CompanySize.Big
                            ),
                            "C++ developer",
                            PositionLevel.Lead,
                            100500,
                            420
                        )
                    )
                )
            )
        )

        return true
    }
}