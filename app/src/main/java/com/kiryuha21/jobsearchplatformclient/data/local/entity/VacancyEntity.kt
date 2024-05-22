package com.kiryuha21.jobsearchplatformclient.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience

@Entity(
    tableName = "vacancy",
)
data class VacancyEntity(
    @PrimaryKey val id: String,
    val username: String,
    val unixSeconds: Long,
    val title: String,
    val description: String,
    val company: Company,
    val minSalary: String,
    val maxSalary: String,
    val requiredWorkExperience: List<WorkExperience>,
    val requiredSkills: List<Skill>,
    val publicationStatus: PublicationStatus,
    val imageUrl: String? = null
)