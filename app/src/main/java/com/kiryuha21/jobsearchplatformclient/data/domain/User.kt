package com.kiryuha21.jobsearchplatformclient.data.domain

enum class UserRole {
    Employer,
    Worker
}

enum class UserStatus {
    Active,
    Frozen,
    Banned
}
class User(
    val email: String,
    val username: String,
    val role: UserRole,
    val userStatus: UserStatus,
    val imageUrl: String?
)
