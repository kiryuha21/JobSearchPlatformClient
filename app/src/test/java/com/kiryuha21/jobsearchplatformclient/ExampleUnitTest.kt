package com.kiryuha21.jobsearchplatformclient

import android.util.Log
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.remote.AuthToken
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject.userRetrofit
import com.kiryuha21.jobsearchplatformclient.util.DEBUG_TAG
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
                Log.i(DEBUG_TAG, e.message.toString())
            }
        }
        val createdUser = runTest {
            try {
                userRetrofit.getUserByUsername(user.username)
            } catch (e: Exception) {
                Log.i(DEBUG_TAG, e.message.toString())
            }
        }
        assertEquals(newUser, createdUser)

        runTest {
            try {
                AuthToken.createToken(user.username, user.password)
                assertNotNull(AuthToken.getToken())
            } catch (e: Exception) {
                Log.i(DEBUG_TAG, e.message.toString())
            }
        }

        runTest {
            try {
                userRetrofit.deleteUserByUsername(user.username, "Bearer ${AuthToken.getToken()}")
            } catch (e: Exception) {
                Log.i(DEBUG_TAG, e.message.toString())
            }
        }

        assertThrows(Exception::class.java) {
            runTest {
                try {
                    userRetrofit.getUserByUsername(user.username)
                } catch (e: Exception) {
                    Log.i(DEBUG_TAG, e.message.toString())
                }
            }
        }
    }
}