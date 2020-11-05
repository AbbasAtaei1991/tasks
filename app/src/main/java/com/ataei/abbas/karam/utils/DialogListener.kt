package com.ataei.abbas.karam.utils

interface DialogListener {
    fun onDismiss(title: String, ransom: String, repeat: Boolean)
}