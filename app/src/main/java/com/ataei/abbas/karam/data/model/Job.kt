package com.ataei.abbas.karam.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class Job(
    @PrimaryKey(autoGenerate = true)
    val id: Int?, //date-time
    val title: String,
    val ransom: Int,
    val done: Boolean,
    val date: String,
    val repeat: Boolean,
    val dayId: String,
    val pay: Boolean = false
)