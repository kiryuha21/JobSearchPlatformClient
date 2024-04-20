package com.kiryuha21.jobsearchplatformclient

import android.util.Log
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.remote.AuthToken
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject
import com.kiryuha21.jobsearchplatformclient.data.remote.api.UserAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test

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
    fun createGetDeleteUser() {
        val userRetrofit = try {
            RetrofitObject.retrofit.create(UserAPI::class.java)
        } catch (e: Exception) {
            Log.i("tag1", e.message.toString())
            throw Exception(e.message)
        }
        val user = UserDTO.SignUpUserDTO(
            email = "test@gmail.com",
            username = "my_example_login",
            password = "secure_one",
            role = UserRole.Worker
        )

        val newUser = runTest {
            try {
                userRetrofit.createNewUser(user)
            } catch (e: Exception) {
                Log.i("tag1", e.message.toString())
                throw Exception(e.message)
            }
        }
        val createdUser = runTest {
            try {
                userRetrofit.getUserByUsername("my_example_login")
            } catch (e: Exception) {
                Log.i("tag1", e.message.toString())
                throw Exception(e.message)
            }
        }
        assertEquals(newUser, createdUser)

        runTest {
            try {
                AuthToken.createToken("my_example_login", "secure_one")
            } catch (e: Exception) {
                Log.i("tag1", e.message.toString())
                throw Exception(e.message)
            }
            assertNotNull(AuthToken.getToken())
        }

        runTest {
            try {
                userRetrofit.deleteUserByUsername(
                    "my_example_login",
                    "Bearer ${AuthToken.getToken()}"
                )
            } catch (e: Exception) {
                Log.i("tag1", e.message.toString())
                throw Exception(e.message)
            }
        }
        assertThrows(Exception::class.java) {
            runTest {
                userRetrofit.getUserByUsername("my_example_login")
            }
        }
    }
}