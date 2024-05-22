package com.kiryuha21.jobsearchplatformclient

import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainVacancy
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.jobApplicationRetrofit
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.resumeRetrofit
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.userRetrofit
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.vacancyRetrofit
import com.kiryuha21.jobsearchplatformclient.util.FakeData.randomJobApplication
import com.kiryuha21.jobsearchplatformclient.util.FakeData.randomResume
import com.kiryuha21.jobsearchplatformclient.util.FakeData.randomUser
import com.kiryuha21.jobsearchplatformclient.util.FakeData.randomVacancy
import com.kiryuha21.jobsearchplatformclient.util.FakeData.withUser
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiTesting {
    @Test
    fun serverIsAlive() = runTest {
        val client = OkHttpClient()
        val request = Request.Builder().url(RetrofitObject.BASE_URL).build()
        val response = client.newCall(request).execute()
        assertEquals(response.code, 200)
    }

    @Test
    fun createGetDeleteUser() = runTest {
        val user = randomUser()

        val newUser = userRetrofit.createNewUser(user)
        val createdUser = userRetrofit.getUserByUsername(user.username)
        assertEquals(newUser, createdUser)

        AuthToken.createToken(user.username, user.password)
        val token = AuthToken.getToken()
        assertNotNull(token)

        userRetrofit.deleteUserByUsername(user.username, "Bearer $token")
        val exception = try {
            userRetrofit.getUserByUsername(user.username)
            null
        } catch (e: Exception) {
            e
        }
        assertNotNull(exception)
    }

    @Test
    fun createGetDeleteVacancy() = runTest {
        withUser(UserRole.Employer) { token, _ ->
            val vacancy = randomVacancy()
            val createdVacancy = vacancyRetrofit.createNewVacancy("Bearer $token", vacancy)
            val vacancyWithId = vacancy.copy(id = createdVacancy.id)

            assertEquals(vacancyWithId.toDomainVacancy(), createdVacancy.toDomainVacancy())

            vacancyRetrofit.deleteVacancy("Bearer $token", createdVacancy.id)
            val exception = try {
                vacancyRetrofit.getPublicVacancyById(createdVacancy.id)
                null
            } catch (e: Exception) {
                e
            }
            assertNotNull(exception)
        }
    }

    @Test
    fun createGetDeleteResume() = runTest {
        withUser(UserRole.Worker) { token, _ ->
            val resume = randomResume()
            val createdResume = resumeRetrofit.createNewResume("Bearer $token", resume)
            val resumeWithId = resume.copy(id = createdResume.id)

            assertEquals(resumeWithId.toDomainResume(), createdResume.toDomainResume())

            resumeRetrofit.deleteResume("Bearer $token", createdResume.id)
            val exception = try {
                resumeRetrofit.getPublicResumeById(createdResume.id)
                null
            } catch (e: Exception) {
                e
            }
            assertNotNull(exception)
        }
    }

    @Test
    fun createJobApplication() = runTest {
        val worker = randomUser(UserRole.Worker)
        userRetrofit.createNewUser(worker)

        AuthToken.createToken(worker.username, worker.password)
        val workerToken = AuthToken.getToken()

        val employer = randomUser(UserRole.Employer)
        userRetrofit.createNewUser(employer)

        AuthToken.createToken(employer.username, employer.password)
        val employerToken = AuthToken.getToken()

        val createdResume = resumeRetrofit.createNewResume("Bearer $workerToken", randomResume())

        val createdVacancy = vacancyRetrofit.createNewVacancy("Bearer $employerToken", randomVacancy())

        jobApplicationRetrofit.createNewJobApplication(
            authToken = "Bearer $employerToken",
            jobApplicationDTO = randomJobApplication(
                senderUsername = employer.username,
                resumeId = createdResume.id,
                vacancyId = createdVacancy.id
            ),
        )

        val unseen = jobApplicationRetrofit.countUnseenJobApplications("Bearer $workerToken")
        val unnotified = jobApplicationRetrofit.countUnnotifiedJobApplications("Bearer $workerToken")

        assertEquals(unseen, 1)
        assertEquals(unnotified, 1)

        userRetrofit.deleteUserByUsername(worker.username, "Bearer $workerToken")
        userRetrofit.deleteUserByUsername(employer.username, "Bearer $workerToken")
    }
}