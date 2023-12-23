package com.kiryuha21.jobsearchplatformclient.data.model

data class Vacancy(
    val title: String,
    val description: String,
    val company: String,
    val minSalary: Int,
    val maxSalary: Int
)
