package com.kiryuha21.jobsearchplatformclient.data.domain.filters

import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience

// TODO
data class VacancyFilters(
    val title: String = "",
    val minSalary: Int = 0,
    val maxSalary: Int = Int.MAX_VALUE,
    val requiredSkills: List<Skill> = emptyList(),
    val requiredWorkExperience: List<WorkExperience> = emptyList(),
    val pageRequestFilter: PageRequestFilter = PageRequestFilter()
)
