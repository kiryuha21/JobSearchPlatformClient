package com.kiryuha21.jobsearchplatformclient.data.domain

enum class UserRole {
    Employer,
    Worker
}
open class BaseUser(
    val email: String,
    val login: String,
    val password: String,
    val role: UserRole
)
