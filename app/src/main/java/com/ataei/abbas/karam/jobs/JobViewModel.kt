package com.ataei.abbas.karam.jobs

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ataei.abbas.karam.data.model.Job
import kotlinx.coroutines.launch

class JobViewModel @ViewModelInject constructor(
    private val repository: JobRepository
) : ViewModel() {
    val jobs: LiveData<List<Job>> = repository.getJobs().asLiveData()

    fun getJobsByDate(date: String): LiveData<List<Job>> = repository.getJobsByDate(date).asLiveData()

    fun getJobsByStatus(status: Boolean): LiveData<List<Job>> = repository.getJobsByStatus(status).asLiveData()

    fun getJob(id: Int) = repository.getJob(id)

    fun insertJob(job: Job) = viewModelScope.launch { repository.insertJob(job) }

    fun updateJob(job: Job) = viewModelScope.launch { repository.updateJob(job) }

    fun deleteJob(job: Job) = viewModelScope.launch { repository.deleteJob(job) }

}