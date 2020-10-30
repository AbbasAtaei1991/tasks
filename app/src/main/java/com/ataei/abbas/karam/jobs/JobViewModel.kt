package com.ataei.abbas.karam.jobs

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataei.abbas.karam.data.model.Job
import kotlinx.coroutines.launch

class JobViewModel @ViewModelInject constructor(
    private val repository: JobRepository
) : ViewModel() {
    val jobs = repository.getJobs()

    fun insertJob(job: Job) = viewModelScope.launch { repository.insertJob(job) }

    fun updateJob(job: Job) = viewModelScope.launch { repository.updateJob(job) }

    fun deleteJob(job: Job) = viewModelScope.launch { repository.deleteJob(job) }

}