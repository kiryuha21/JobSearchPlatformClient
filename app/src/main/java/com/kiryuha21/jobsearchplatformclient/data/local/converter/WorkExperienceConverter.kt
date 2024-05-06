package com.kiryuha21.jobsearchplatformclient.data.local.converter

import androidx.room.TypeConverter
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WorkExperienceConverter {
    @TypeConverter
    fun workExperienceToString(workExperience: List<WorkExperience>) : String = Json.encodeToString(workExperience)

    @TypeConverter
    fun stringToWorkExperience(string: String) : List<WorkExperience> = Json.decodeFromString(string)
}