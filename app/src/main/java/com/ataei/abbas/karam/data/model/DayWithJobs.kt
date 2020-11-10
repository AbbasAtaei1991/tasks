package com.ataei.abbas.karam.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.ataei.abbas.karam.utils.ExpandableRecyclerViewAdapter

data class DayWithJobs(
    @Embedded
    val day: Day,
    @Relation(
        entity = Job::class,
        parentColumn = "date",
        entityColumn = "dayId"
    )
    val jobs: List<Job>
)