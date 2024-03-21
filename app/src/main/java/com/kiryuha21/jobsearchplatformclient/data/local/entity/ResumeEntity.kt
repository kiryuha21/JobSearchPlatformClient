package com.kiryuha21.jobsearchplatformclient.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "resume"
)
class ResumeEntity(
    @PrimaryKey val id: String,
)