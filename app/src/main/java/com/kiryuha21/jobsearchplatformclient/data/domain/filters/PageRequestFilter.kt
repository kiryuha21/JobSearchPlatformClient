package com.kiryuha21.jobsearchplatformclient.data.domain.filters

enum class SortingDirection {
    ASC,
    DESC
}

const val DEFAULT_SORT_PROPERTY = "placedAt"

data class PageRequestFilter(
    val pageNumber: Int = 0,
    val pageSize: Int = 10,
    val sortingDirection: SortingDirection = SortingDirection.ASC,
    val sortProperty: String = DEFAULT_SORT_PROPERTY
)
