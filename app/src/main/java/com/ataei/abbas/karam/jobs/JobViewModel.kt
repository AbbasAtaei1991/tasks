package com.ataei.abbas.karam.jobs

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ataei.abbas.karam.data.model.Daily
import com.ataei.abbas.karam.data.model.Day
import com.ataei.abbas.karam.data.model.DayWithJobs
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.utils.UserPreferenceRepository
import kotlinx.coroutines.launch

class JobViewModel @ViewModelInject constructor(
    private val repository: JobRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {
    val jobs: LiveData<List<Job>> = repository.getJobs().asLiveData()

    fun getJobsByDate(date: String): LiveData<List<Job>> = repository.getJobsByDate(date).asLiveData()

    fun insertDay(day: Day) = viewModelScope.launch { repository.insertDay(day) }

    fun insertJob(job: Job) = viewModelScope.launch { repository.insertJob(job) }

    fun updateJob(job: Job) = viewModelScope.launch { repository.updateJob(job) }

    fun deleteJob(job: Job) = viewModelScope.launch { repository.deleteJob(job) }

    val items: LiveData<List<Daily>> = repository.getAll().asLiveData()

    val savedRansom: LiveData<String> = userPreferenceRepository.getRansom().asLiveData()

    fun clear(date: String) = repository.clear(date)

}