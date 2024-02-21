package com.kiryuha21.jobsearchplatformclient.data.domain

enum class PositionLevel {
    Junior, Middle, Senior, Lead
}

data class WorkExperience(
    val company: Company,
    val position: String,
    val positionLevel: PositionLevel,
    val salary: Int,
    val months: Int
)
