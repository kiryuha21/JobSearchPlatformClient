package com.kiryuha21.jobsearchplatformclient.data.domain

import kotlinx.serialization.Serializable

enum class PositionLevel {
    Junior, Middle, Senior, Lead
}

@Serializable
data class WorkExperience(
    val company: Company,
    val position: String,
    val positionLevel: PositionLevel,
    val salary: Int,
    val months: Int
) {
    fun workMonthsFormatted() =
        "${months / 12} лет и ${months % 12} месяцев"

    fun positionFormatted() =
        "${positionLevel.name} $position"

    fun formattedAsRequirement() =
        "${positionFormatted()} в течение ${workMonthsFormatted()}"

    override fun toString() =
        "${positionFormatted()} в ${company.name} в течение ${workMonthsFormatted()}"
}
