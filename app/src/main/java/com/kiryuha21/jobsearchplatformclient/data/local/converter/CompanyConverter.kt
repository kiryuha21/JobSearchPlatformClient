package com.kiryuha21.jobsearchplatformclient.data.local.converter

import androidx.room.TypeConverter
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CompanyConverter {
    @TypeConverter
    fun companyToString(company: Company) : String = Json.encodeToString(company)

    @TypeConverter
    fun stringToCompany(string: String) : Company = Json.decodeFromString(string)
}