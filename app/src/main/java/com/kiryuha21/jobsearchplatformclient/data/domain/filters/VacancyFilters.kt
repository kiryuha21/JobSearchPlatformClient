package com.kiryuha21.jobsearchplatformclient.data.domain.filters

import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.PageRequestFilter

data class VacancyFilters(
    val placedAt: Long? = null,
    val query: String? = null,
    val minSalary: Int? = null,
    val maxSalary: Int? = null,
    val requiredSkills: List<Skill>? = null,
    val requiredWorkExperience: List<WorkExperience>? = null,
    val pageRequestFilter: PageRequestFilter = PageRequestFilter()
)
