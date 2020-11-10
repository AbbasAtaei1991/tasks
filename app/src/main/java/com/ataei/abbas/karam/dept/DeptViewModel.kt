package com.ataei.abbas.karam.dept

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataei.abbas.karam.data.model.DayWithJobs
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.jobs.JobRepository
import kotlinx.coroutines.launch

class DeptViewModel @ViewModelInject constructor(
    private val repository: JobRepository
) : ViewModel() {

    fun getDays(): List<DayWithJobs> = repository.getDays()

    fun updateJob(job: Job) = viewModelScope.launch { repository.updateJob(job) }
}