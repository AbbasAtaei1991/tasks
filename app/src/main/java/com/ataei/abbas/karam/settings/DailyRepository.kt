package com.ataei.abbas.karam.settings

import com.ataei.abbas.karam.data.dao.DailyDao
import com.ataei.abbas.karam.data.model.Daily
import javax.inject.Inject

class DailyRepository @Inject constructor(
    private val dailyDao: DailyDao
) {
}