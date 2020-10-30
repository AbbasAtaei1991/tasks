package com.ataei.abbas.karam.jobs

import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.data.model.JobDao
import javax.inject.Inject

class JobRepository @Inject constructor(
    private val localDataSource: JobDao
) {
    fun getJobs() = localDataSource.getAllJobs()

    suspend fun insertJob(job: Job) = localDataSource.insert(job)

    suspend fun updateJob(job: Job) = localDataSource.updateJob(job)

    suspend fun deleteJob(job: Job) = localDataSource.deleteJob(job)
}