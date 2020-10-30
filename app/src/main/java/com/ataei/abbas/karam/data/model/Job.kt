package com.ataei.abbas.karam.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class Job(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val done: Boolean
)