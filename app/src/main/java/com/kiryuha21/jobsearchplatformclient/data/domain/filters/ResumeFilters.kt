package com.kiryuha21.jobsearchplatformclient.data.domain.filters

import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience

data class ResumeFilters(
    val placedAt: Long? = null,
    val youngerThan: Int? = null,
    val olderThan: Int? = null,
    val applyPosition: String? = null,
    val isImageSet: Boolean? = null,
    val skills: List<Skill>? = null,
    val workExperience: List<WorkExperience>? = null,

    val pageRequestFilter: PageRequestFilter = PageRequestFilter()
)
