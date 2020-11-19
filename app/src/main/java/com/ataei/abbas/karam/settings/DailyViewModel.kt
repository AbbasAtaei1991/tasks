package com.ataei.abbas.karam.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ataei.abbas.karam.data.model.Daily
import com.ataei.abbas.karam.jobs.JobRepository
import kotlinx.coroutines.launch

class DailyViewModel @ViewModelInject constructor(
    private val repository: JobRepository
) : ViewModel() {
    val items: LiveData<List<Daily>> = repository.getAll().asLiveData()

    fun search(title: String) = repository.search(title)

    fun searchDaily(title: String) = repository.searchDaily(title)

    fun insert(daily: Daily) = viewModelScope.launch { repository.insert(daily) }

    fun update(daily: Daily) = viewModelScope.launch { repository.update(daily) }

    fun delete(daily: Daily) = viewModelScope.launch { repository.delete(daily) }
}