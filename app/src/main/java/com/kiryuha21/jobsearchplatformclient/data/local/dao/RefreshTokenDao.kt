package com.kiryuha21.jobsearchplatformclient.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RefreshTokenDao {
    @Insert
    fun setToken(token: String)

    @Update
    fun updateToken(newToken: String)

    @Query("SELECT token FROM refreshToken LIMIT 1")
    fun getToken(): Flow<String>
}