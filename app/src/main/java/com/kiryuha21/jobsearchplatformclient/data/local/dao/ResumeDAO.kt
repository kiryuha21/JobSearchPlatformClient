package com.kiryuha21.jobsearchplatformclient.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.DEFAULT_PAGE_SIZE
import com.kiryuha21.jobsearchplatformclient.data.local.EXPIRATION_TIME_SECONDS
import com.kiryuha21.jobsearchplatformclient.data.local.entity.ResumeEntity

@Dao
interface ResumeDAO {
    @Upsert
    fun upsertResumes(resumes: List<ResumeEntity>)

    @Query("DELETE FROM resume WHERE :now - resume.unixSeconds > $EXPIRATION_TIME_SECONDS")
    fun deleteExpiredResumes(now: Long)

    @Query("SELECT * FROM resume WHERE resume.username = :username LIMIT $DEFAULT_PAGE_SIZE OFFSET :offset")
    fun getUserResumes(username: String, offset: Int) : List<ResumeEntity>
}