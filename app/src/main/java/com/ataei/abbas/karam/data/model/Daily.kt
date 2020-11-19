package com.ataei.abbas.karam.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily")
data class Daily(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String
)