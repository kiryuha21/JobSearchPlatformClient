package com.kiryuha21.jobsearchplatformclient

import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitEntity
import com.kiryuha21.jobsearchplatformclient.data.remote.ResumeAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.BaseUserDTO
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun userRetrofitWorks() = runTest {
        val userRetrofit = RetrofitEntity.retrofit.create(ResumeAPI::class.java)
        val user = BaseUserDTO(
            email = "test@gmail.com",
            login = "my_example_login",
            password = "secure_one"
        )

        val newUser = userRetrofit.createNewWorker(user).execute().body()!!
        val createdUser = userRetrofit.getUsers().execute().body()!!.first()
        assertEquals(newUser, createdUser)
    }
}