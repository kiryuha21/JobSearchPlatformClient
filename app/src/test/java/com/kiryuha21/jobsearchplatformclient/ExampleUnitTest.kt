package com.kiryuha21.jobsearchplatformclient

import android.net.http.HttpException
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
    fun createGetDeleteUser() = runTest {
        try {
            val userRetrofit = RetrofitObject.retrofit.create(UserAPI::class.java)
            val user = UserDTO.SignUpUserDTO(
                email = "test@gmail.com",
                username = "my_example_login",
                password = "secure_one",
                role = UserRole.Worker
            )

            val newUser = userRetrofit.createNewUser(user)
            val createdUser = userRetrofit.getUserByUsername("my_example_login")
            // assertEquals(newUser, createdUser)

            AuthToken.createToken("my_example_login", "secure_one")
            // assertNotNull(AuthToken.getToken())

            userRetrofit.deleteUserByUsername("my_example_login", "Bearer ${AuthToken.getToken()}")
            userRetrofit.getUserByUsername("my_example_login")
//            assertThrows(Exception::class.java) {
//                runTest {
//                    userRetrofit.getUserByUsername("my_example_login")
//                }
//            }
        } catch (e: HttpException) {
            println("HERE: reason is ${e.message}")
            e.message?.let { Log.d("tag1", it) }
        } catch (e: Exception) {
            println("HERE: reason is ${e.message}")
        }

    }
}