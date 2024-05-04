package com.kiryuha21.jobsearchplatformclient.data.domain.filters

enum class SortingDirection {
    ASC,
    DESC
}

enum class MoreItemsState {
    Available,
    Unavailable,
    Unreachable,
    Undefined
}

const val DEFAULT_SORT_PROPERTY = "placedAt"
const val DEFAULT_PAGE_SIZE = 10
const val START_PAGE = 0

data class PageRequestFilter(
    val pageNumber: Int = START_PAGE,
    val pageSize: Int = DEFAULT_PAGE_SIZE,
    val sortingDirection: SortingDirection = SortingDirection.ASC,
    val sortProperty: String = DEFAULT_SORT_PROPERTY
)
