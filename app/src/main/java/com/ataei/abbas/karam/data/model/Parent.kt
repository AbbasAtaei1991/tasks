package com.ataei.abbas.karam.data.model

import com.ataei.abbas.karam.utils.ExpandableRecyclerViewAdapter

data class Parent(
    val day: Day,
    val jobs: List<Job>
) : ExpandableRecyclerViewAdapter.ExpandableGroup<Job>() {
    override fun getExpandingItems(): List<Job> {
        return jobs
    }
}