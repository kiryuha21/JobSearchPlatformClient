package com.kiryuha21.jobsearchplatformclient.data.domain.filters

object FilterName {
    object Resume {
        const val PLACED_AT = "placedAt"
        const val AGE = "age"
        const val APPLY_POSITION = "applyPosition"
        const val IS_IMAGE_SET = "isImageSet"
    }

    object Vacancy {
        const val PLACED_AT = "placedAt"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val MIN_SALARY = "minSalary"
        const val MAX_SALARY = "maxSalary"
    }
}

data class FilterSortOption(
    val description: String,
    val visibleName: String,
)