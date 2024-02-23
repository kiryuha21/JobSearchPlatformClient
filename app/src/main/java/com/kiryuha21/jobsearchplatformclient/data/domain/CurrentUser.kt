package com.kiryuha21.jobsearchplatformclient.data.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object CurrentUser {
    var userInfo: MutableState<User> = mutableStateOf(User(-1, "", "", emptyList()))
        private set

    suspend fun tryLogIn(login: String, password: String): Boolean {
        // TODO: make real api call
        userInfo.value = User(
            1,
            "test",
            "roflanchik",
            listOf(
                Resume(
                    "John",
                    "Smit",
                    "12909483",
                    "hey@gmail.com",
                    "Senior C++ developer",
                    listOf(
                        Skill(
                            "C++ development",
                            SkillLevel.HasCommercialProjects
                        ),
                        Skill(
                            "Git workflows",
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