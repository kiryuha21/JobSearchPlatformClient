package com.kiryuha21.jobsearchplatformclient.data.domain

enum class UserRole {
    Employer,
    Worker
}
class User(
    val email: String,
    val username: String,
    val role: UserRole,
    val imageUrl: String?
)
