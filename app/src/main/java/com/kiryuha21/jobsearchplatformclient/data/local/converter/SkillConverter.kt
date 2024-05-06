package com.kiryuha21.jobsearchplatformclient.data.local.converter

import androidx.room.TypeConverter
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SkillConverter {
    @TypeConverter
    fun skillsToString(skills: List<Skill>) : String = Json.encodeToString(skills)

    @TypeConverter
    fun stringToSkills(string: String) : List<Skill> = Json.decodeFromString(string)
}