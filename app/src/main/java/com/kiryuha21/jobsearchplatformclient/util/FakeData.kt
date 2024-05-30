package com.kiryuha21.jobsearchplatformclient.util

import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.JobApplicationDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject
import kotlin.random.Random

object FakeData {
    private val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    private val digits = ('0'..'9')

    fun randomString(length: Int) = (1..length).map { allowedChars.random() }.joinToString("")
    private fun randomPhoneNumber() = (1..11).map { digits.random() }.joinToString("")

    fun randomUser(
        role: UserRole = if (Random.nextInt(2) == 0) UserRole.Worker else UserRole.Employer
    ) : UserDTO.SignUpUserDTO {
        val username = randomString(10)

        val mail = "$username@gmail.com"
        val password = "password"

        return UserDTO.SignUpUserDTO(
            username = username,
            email = mail,
            role = role,
            password = password
        )
    }

    fun randomVacancy() : Vacancy {
        val minSalary = Random.nextInt(500000)
        val maxSalary = minSalary + Random.nextInt(500000)

        return Vacancy(
            id = "",
            title = randomString(10),
            description = randomString(30),
            company = Company(randomString(20)),
            minSalary = minSalary.toString(),
            maxSalary = maxSalary.toString(),
            publicationStatus = PublicationStatus.Published
        )
    }

    fun randomResume() : Resume {
        return Resume(
            id = "",
            firstName = randomString(10),
            lastName = randomString(10),
            birthDate = 0,
            phoneNumber = randomPhoneNumber(),
            contactEmail = "${randomString(10)}@gmail.com",
            applyPosition = randomString(10),
            publicationStatus = PublicationStatus.Published
        )
    }

    fun randomJobApplication(resumeId: String, vacancyId: String) =
        JobApplicationDTO.JobApplicationRequestDTO(
            referenceResumeId = resumeId,
            referenceVacancyId = vacancyId,
            message = randomString(10),
        )

    suspend fun withUser(role: UserRole, testBody: suspend (String, UserDTO.SignUpUserDTO) -> Unit) {
        val user = randomUser(role)
        RetrofitObject.userRetrofit.createNewUser(user)

        AuthToken.createToken(user.username, user.password)
        val token = AuthToken.getToken()

        testBody(token.toString(), user)

        RetrofitObject.userRetrofit.deleteUserByUsername(user.username, "Bearer $token")
    }
}
