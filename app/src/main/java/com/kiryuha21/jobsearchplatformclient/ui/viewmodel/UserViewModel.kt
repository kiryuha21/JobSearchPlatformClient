package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import com.kiryuha21.jobsearchplatformclient.data.model.Company
import com.kiryuha21.jobsearchplatformclient.data.model.CompanySize
import com.kiryuha21.jobsearchplatformclient.data.model.PositionLevel
import com.kiryuha21.jobsearchplatformclient.data.model.Resume
import com.kiryuha21.jobsearchplatformclient.data.model.Skill
import com.kiryuha21.jobsearchplatformclient.data.model.SkillLevel
import com.kiryuha21.jobsearchplatformclient.data.model.User
import com.kiryuha21.jobsearchplatformclient.data.model.WorkExperience
import com.kiryuha21.jobsearchplatformclient.ui.contract.UserContract

// TODO: implement methods
class UserViewModel : BaseViewModel<UserContract.UserIntent, UserContract.UserState>() {
    override fun initialState(): UserContract.UserState {
        return UserContract.UserState.Unauthorized
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is UserContract.UserIntent.TryLogIn -> {

            }
            is UserContract.UserIntent.TrySignUp -> {

            }
            is UserContract.UserIntent.TryResetPassword -> {

            }
        }
    }

    private fun tryLogIn(login: String, password: String): Boolean {
        _viewState.value = UserContract.UserState.Authorized(
            User(
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
        )

        return true
    }

    private fun trySignUp(login: String, email: String, password: String): Boolean {
        return true
    }

    private fun tryResetPassword(email: String): Boolean {
        return true
    }
}