package com.kiryuha21.jobsearchplatformclient.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience

@Entity(
    tableName = "resume"
)
class ResumeEntity(
    @PrimaryKey val id: String,
    val username: String,
    val unixSeconds: Long,
    val firstName: String,
    val lastName: String,
    val birthDate: Long,
    val phoneNumber: String,
    val contactEmail: String,
    val applyPosition: String,
    val skills: List<Skill>,
    val workExperience: List<WorkExperience>,
    val publicationStatus: PublicationStatus,
    val imageUrl: String? = null
)