package com.ataei.abbas.karam.jobs

import com.ataei.abbas.karam.data.dao.DailyDao
import com.ataei.abbas.karam.data.model.Day
import com.ataei.abbas.karam.data.dao.DayDao
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.data.dao.JobDao
import com.ataei.abbas.karam.data.model.Daily
import javax.inject.Inject

class JobRepository @Inject constructor(
    private val jobDao: JobDao,
    private val dayDao: DayDao,
    private val dailyDao: DailyDao
) {
    fun getJobs() = jobDao.getAllJobs()

    fun getDays() = dayDao.getDayWithJobs()

    fun getDay(date: String) = dayDao.getDayWithJobsByDate(date)

    suspend fun insertDay(day: Day) = dayDao.insertDay(day)

    fun getLastJob() = jobDao.getLastRecord()

    fun getJob(id: Int) = jobDao.getJob(id)

    fun getJobsByDate(date: String) = jobDao.getJobByDate(date)

    fun getJobsByStatus(status: Boolean) = jobDao.getJobByStatus(status)

    fun getJobsBy(status: Boolean) = jobDao.getJobsBy(status)

    suspend fun insertAll(jobs: List<Job>) = jobDao.insertAll(jobs)

    suspend fun insertJob(job: Job) = jobDao.insert(job)

    suspend fun updateJob(job: Job) = jobDao.updateJob(job)

    suspend fun deleteJob(job: Job) = jobDao.deleteJob(job)

    fun getAll() = dailyDao.getAllItems()

    fun search(title: String) = dailyDao.search(title)

    fun searchDaily(title: String) = dailyDao.searchDaily(title)

    suspend fun insert(daily: Daily) = dailyDao.insert(daily)

    suspend fun update(daily: Daily) = dailyDao.update(daily)

    suspend fun delete(daily: Daily) = dailyDao.delete(daily)

    fun clear() = jobDao.clear()
}