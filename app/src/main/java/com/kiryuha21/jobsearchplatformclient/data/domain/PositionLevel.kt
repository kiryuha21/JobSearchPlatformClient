package com.kiryuha21.jobsearchplatformclient.data.domain

enum class PositionLevel {
    Junior, Middle, Senior, Lead
}

val descriptionToPositionLevel = mapOf(
    "Джуниор" to PositionLevel.Junior,
    "Мидл" to PositionLevel.Middle,
    "Сеньор" to PositionLevel.Senior,
    "Лид" to PositionLevel.Lead
)