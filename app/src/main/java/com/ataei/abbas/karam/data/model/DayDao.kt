package com.ataei.abbas.karam.data.model

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: Day)

    @Transaction
    @Query("SELECT * FROM days")
    fun getDayWithJobs(): List<DayWithJobs>

    @Transaction
    @Query("SELECT * FROM days WHERE date = :date LIMIT 1")
    fun getDayWithJobsByDate(date: String): DayWithJobs
}