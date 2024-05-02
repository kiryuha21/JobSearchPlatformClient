package com.kiryuha21.jobsearchplatformclient.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.DEFAULT_SORT_PROPERTY
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.SortingDirection

data class PageRequestFilterDTO(
    @SerializedName("pageNumber")
    val pageNumber: Int = 0,
    @SerializedName("pageSize")
    val pageSize: Int = 10,
    @SerializedName("sortDirection")
    val sortingDirection: SortingDirection = SortingDirection.ASC,
    @SerializedName("sortProperties")
    val sortProperty: String = DEFAULT_SORT_PROPERTY
)
