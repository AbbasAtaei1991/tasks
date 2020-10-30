package com.ataei.abbas.karam.utils

import android.view.View
import com.ataei.abbas.karam.data.model.Job

interface OnStatusClickListener {
    fun onStatusClicked(job: Job, position: Int, isDone: Boolean)

    fun onMenuClicked(job: Job, view: View)
}