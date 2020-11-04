package com.ataei.abbas.karam.jobs

import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.data.model.JobDao
import javax.inject.Inject

class JobRepository @Inject constructor(
    private val jobDao: JobDao
) {
    fun getJobs() = jobDao.getAllJobs()

    fun getLastJob() = jobDao.getLastRecord()

    fun getJob(id: Int) = jobDao.getJob(id)

    fun getJobsByDate(date: String) = jobDao.getJobByDate(date)

    fun getJobsByStatus(status: Boolean) = jobDao.getJobByStatus(status)

    suspend fun insertAll(jobs: List<Job>) = jobDao.insertAll(jobs)

    suspend fun insertJob(job: Job) = jobDao.insert(job)

    suspend fun updateJob(job: Job) = jobDao.updateJob(job)

    suspend fun deleteJob(job: Job) = jobDao.deleteJob(job)
}