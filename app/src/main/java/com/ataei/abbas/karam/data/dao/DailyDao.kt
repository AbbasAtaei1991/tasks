package com.ataei.abbas.karam.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ataei.abbas.karam.data.model.Daily
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyDao {

    @Query("SELECT * FROM daily")
    fun getAllItems() : Flow<List<Daily>>

    @Query("SELECT * FROM daily WHERE title LIKE '%' || :title || '%'")
    fun search(title: String) : LiveData<List<Daily>>

    @Query("SELECT * FROM daily WHERE title = :title")
    fun searchDaily(title: String) : LiveData<List<Daily>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(daily: Daily)

    @Update
    suspend fun update(daily: Daily)

    @Delete
    suspend fun delete(daily: Daily)
}