package com.kiryuha21.jobsearchplatformclient.data.domain

enum class UserRole {
    Employer,
    Worker
}
class BaseUser(
    val email: String,
    val username: String,
    val password: String,
    val role: UserRole
)
