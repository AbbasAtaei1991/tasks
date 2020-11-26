package com.ataei.abbas.karam.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days")
data class Day(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: String, // date
    val debt: Int,
    val pay: Boolean
)