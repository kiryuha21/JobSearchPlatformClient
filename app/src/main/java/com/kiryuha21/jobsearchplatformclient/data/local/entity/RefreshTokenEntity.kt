package com.kiryuha21.jobsearchplatformclient.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "refreshToken"
)
data class RefreshTokenEntity(
    @PrimaryKey
    val token: String = ""
)