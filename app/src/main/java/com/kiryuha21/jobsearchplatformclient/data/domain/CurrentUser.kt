package com.kiryuha21.jobsearchplatformclient.data.domain

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainUser
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitEntity
import com.kiryuha21.jobsearchplatformclient.data.remote.UserApi
import com.kiryuha21.jobsearchplatformclient.data.remote.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.Date

object CurrentUser {
    private val userRetrofit = RetrofitEntity.retrofit.create(UserApi::class.java)
    var userInfo: MutableState<BaseUser> = mutableStateOf(BaseUser("", ""))
        private set

    suspend fun tryLogIn(login: String, password: String): Boolean {
        // TODO: make real api call
        userInfo.value = Worker(
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

    suspend fun trySignUp(email: String, login: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val response = userRetrofit.createNewUser(
                    UserDTO(
                        email = email,
                        login = login,
                        password = password
                    )
                ).execute()
                Log.d("tag1", response.toString())
                true
            } catch (e: IOException) {
                Log.d("tag1", e.toString())
                false
            } catch (e: HttpException) {
                false
            }
        }
}