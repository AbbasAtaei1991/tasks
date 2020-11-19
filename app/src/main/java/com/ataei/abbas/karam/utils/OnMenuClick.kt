package com.ataei.abbas.karam.utils

import android.view.View
import com.ataei.abbas.karam.data.model.Daily

interface OnMenuClick {
    fun onMenuClicked(daily: Daily, view: View)
}