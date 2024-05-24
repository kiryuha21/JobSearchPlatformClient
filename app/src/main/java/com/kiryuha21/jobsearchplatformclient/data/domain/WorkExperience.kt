package com.kiryuha21.jobsearchplatformclient.data.domain

import com.kiryuha21.jobsearchplatformclient.util.isNumeric
import kotlinx.serialization.Serializable

@Serializable
data class WorkExperience(
    val company: Company,
    val position: String,
    val positionLevel: PositionLevel,
    val salary: String,
    val months: String
) {
    fun isValidForResume() =
        company.name.isNotBlank() &&
        position.isNotBlank() &&
        salary.isNotBlank() && salary.isNumeric() &&
        months.isNotBlank() && months.isNumeric()

    fun isValidForVacancy() =
        position.isNotBlank() &&
        months.isNotBlank() && months.isNumeric()

    fun workMonthsFormatted() =
        "${months.toInt() / 12} лет и ${months.toInt() % 12} месяцев"

    fun positionFormatted() =
        "${positionLevel.name} $position"

    fun formattedAsRequirement() =
        "${positionFormatted()} в течение ${workMonthsFormatted()}"

    override fun toString() =
        "${positionFormatted()} в ${company.name} в течение ${workMonthsFormatted()}"
}
