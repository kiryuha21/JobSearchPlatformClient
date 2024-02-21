package com.kiryuha21.jobsearchplatformclient.data.domain

enum class CompanySize {
    Little, Middle, Big
}

data class Company(
    val name: String,
    val size: CompanySize
)
