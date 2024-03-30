package com.kiryuha21.jobsearchplatformclient.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.kiryuha21.jobsearchplatformclient.data.local.entity.RefreshTokenEntity
import com.kiryuha21.jobsearchplatformclient.data.local.entity.ResumeEntity
import com.kiryuha21.jobsearchplatformclient.data.local.entity.VacancyEntity

@Database(entities = [RefreshTokenEntity::class, ResumeEntity::class, VacancyEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}