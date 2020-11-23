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
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class JobViewModel @ViewModelInject constructor(
    private val repository: JobRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {
    val jobs: LiveData<List<Job>> = repository.getJobs().asLiveData()

    val last: LiveData<Job> = repository.getLastJob()

    fun getJobsByDate(date: String): LiveData<List<Job>> = repository.getJobsByDate(date).asLiveData()

    fun insertDay(day: Day) = viewModelScope.launch { repository.insertDay(day) }

    fun getDay(date: String): DayWithJobs = repository.getDay(date)

    fun getDays(): List<DayWithJobs> = repository.getDays()

    fun getJob(id: Int) = repository.getJob(id)

    fun insertJob(job: Job) = viewModelScope.launch { repository.insertJob(job) }

    fun insertAll(jobs: List<Job>) = viewModelScope.launch { repository.insertAll(jobs) }

    fun updateJob(job: Job) = viewModelScope.launch { repository.updateJob(job) }

    fun deleteJob(job: Job) = viewModelScope.launch { repository.deleteJob(job) }

    val items: LiveData<List<Daily>> = repository.getAll().asLiveData()

    val savedRansom: LiveData<String> = userPreferenceRepository.getRansom().asLiveData()

    fun clear() = repository.clear()

}