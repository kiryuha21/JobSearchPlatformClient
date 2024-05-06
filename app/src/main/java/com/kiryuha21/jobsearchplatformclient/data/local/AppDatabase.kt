package com.kiryuha21.jobsearchplatformclient.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kiryuha21.jobsearchplatformclient.data.local.converter.CompanyConverter
import com.kiryuha21.jobsearchplatformclient.data.local.converter.SkillConverter
import com.kiryuha21.jobsearchplatformclient.data.local.converter.TimestampConverter
import com.kiryuha21.jobsearchplatformclient.data.local.converter.WorkExperienceConverter
import com.kiryuha21.jobsearchplatformclient.data.local.dao.ResumeDAO
import com.kiryuha21.jobsearchplatformclient.data.local.dao.VacancyDAO
import com.kiryuha21.jobsearchplatformclient.data.local.entity.ResumeEntity
import com.kiryuha21.jobsearchplatformclient.data.local.entity.VacancyEntity

const val EXPIRATION_TIME_SECONDS = 3600  // 1 hour

@Database(entities = [ResumeEntity::class, VacancyEntity::class], version = 1)
@TypeConverters(CompanyConverter::class, SkillConverter::class, TimestampConverter::class, WorkExperienceConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val vacancyDAO: VacancyDAO
    abstract val resumeDAO: ResumeDAO

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "PublicationsDatabase")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}