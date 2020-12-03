package com.ataei.abbas.karam.data.dao

import androidx.room.*
import com.ataei.abbas.karam.data.model.Day
import com.ataei.abbas.karam.data.model.DayWithJobs

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: Day)

    @Transaction
    @Delete
    suspend fun deleteDay(day: Day)

    @Transaction
    @Query("SELECT * FROM days ORDER BY date DESC LIMIT 30")
    fun getDayWithJobs(): List<DayWithJobs>

    @Transaction
    @Query("SELECT * FROM days WHERE date = :date LIMIT 1")
    fun getDayWithJobsByDate(date: String): DayWithJobs
}