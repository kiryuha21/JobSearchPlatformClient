package com.kiryuha21.jobsearchplatformclient.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kiryuha21.jobsearchplatformclient.data.domain.Company

@Entity(
    tableName = "vacancy",
)
class VacancyEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    //@ColumnInfo(name = "company") val company: Company,
    @ColumnInfo(name = "minSalary") val minSalary: Int,
    @ColumnInfo(name = "maxSalary") val maxSalary: Int
)