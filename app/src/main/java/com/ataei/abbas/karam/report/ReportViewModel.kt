package com.ataei.abbas.karam.report

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ataei.abbas.karam.data.model.Day
import com.ataei.abbas.karam.data.model.DayWithJobs
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.jobs.JobRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ReportViewModel @ViewModelInject constructor(
    private val repository: JobRepository
) : ViewModel() {

    fun getDays(): List<DayWithJobs> = repository.getDays()

    fun updateJob(job: Job) = viewModelScope.launch { repository.updateJob(job) }

    fun deleteDay(day: Day) = viewModelScope.launch { repository.deleteDay(day) }

    fun getJobsByStatus(status: Boolean): LiveData<List<Job>> = repository.getJobsBy(status)

    fun getJobs(): LiveData<List<Job>> = repository.getJobs().asLiveData()

    private val jobFlow: Flow<JobHolder> = repository.getJobs().map { jobHolders ->
        val jobs = jobHolders.filter { job ->
            job.done
        }
        JobHolder(jobs)
    }

    data class JobHolder(val jobs: List<Job>)
}