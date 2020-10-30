package com.ataei.abbas.karam.utils

import com.ataei.abbas.karam.data.model.Job

interface OnStatusClickListener {
    fun onStatusClicked(job: Job, position: Int, isDone: Boolean)
}