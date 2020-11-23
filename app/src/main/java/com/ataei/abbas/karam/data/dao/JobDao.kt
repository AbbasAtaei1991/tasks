package com.ataei.abbas.karam.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ataei.abbas.karam.data.model.Job
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {

    @Query("SELECT * FROM jobs")
    fun getAllJobs() : Flow<List<Job>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(jobs: List<Job>)

    @Query("SELECT * FROM jobs ORDER BY id DESC LIMIT 1")
    fun getLastRecord() : LiveData<Job>

    @Query("SELECT * FROM jobs WHERE done = :done")
    fun getJobsBy(done: Boolean) : LiveData<List<Job>>

    @Query("SELECT * FROM jobs WHERE id = :id")
    fun getJob(id: Int) : LiveData<Job>

    @Query("SELECT * FROM jobs WHERE date = :date")
    fun getJobByDate(date: String) : Flow<List<Job>>

    @Query("SELECT * FROM jobs WHERE done = :status")
    fun getJobByStatus(status: Boolean) : Flow<List<Job>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: Job)

    @Update
    suspend fun updateJob(job: Job)

    @Delete
    suspend fun deleteJob(job: Job)

    @Query("DELETE FROM jobs")
    fun clear()
}