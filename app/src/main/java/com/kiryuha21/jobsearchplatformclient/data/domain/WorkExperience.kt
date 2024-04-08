package com.kiryuha21.jobsearchplatformclient.data.domain

enum class PositionLevel {
    Junior, Middle, Senior, Lead
}

data class WorkExperience(
    var company: Company,
    var position: String,
    var positionLevel: PositionLevel,
    var salary: Int,
    var months: Int
) {
    fun workMonthsFormatted() =
        "${months / 12} лет и ${months % 12} месяцев"

    fun positionFormatted() =
        "${positionLevel.name} $position"

    override fun toString() =
        "${positionFormatted()} в ${company.name} в течение ${workMonthsFormatted()}"
}
