package com.kiryuha21.jobsearchplatformclient.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.DEFAULT_PAGE_SIZE
import com.kiryuha21.jobsearchplatformclient.data.local.EXPIRATION_TIME_SECONDS
import com.kiryuha21.jobsearchplatformclient.data.local.entity.VacancyEntity

@Dao
interface VacancyDAO {
    @Upsert
    fun upsertVacancies(vacancies: List<VacancyEntity>)

    @Query("DELETE FROM vacancy WHERE :now - vacancy.unixSeconds > $EXPIRATION_TIME_SECONDS")
    fun deleteExpiredVacancies(now: Long)

    @Query("SELECT * FROM vacancy WHERE vacancy.username = :username LIMIT $DEFAULT_PAGE_SIZE OFFSET :offset")
    fun getUserVacancies(username: String, offset: Int) : List<VacancyEntity>
}