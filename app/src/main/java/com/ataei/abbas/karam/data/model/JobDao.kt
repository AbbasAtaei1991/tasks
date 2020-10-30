package com.ataei.abbas.karam.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface JobDao {

    @Query("SELECT * FROM jobs")
    fun getAllJobs() : LiveData<List<Job>>

    @Query("SELECT * FROM jobs WHERE id = :id")
    fun getJob(id: Int) : LiveData<Job>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: Job)

    @Update
    suspend fun updateJob(job: Job)

    @Delete
    suspend fun deleteJob(job: Job)
}