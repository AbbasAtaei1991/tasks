package com.ataei.abbas.karam.utils

import com.ataei.abbas.karam.data.model.Job

interface DialogListener {
    fun onDismiss(title: String, ransom: String, isNew: Boolean)
}