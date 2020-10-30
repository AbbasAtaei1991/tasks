package com.ataei.abbas.karam.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ataei.abbas.karam.R
import com.ataei.abbas.karam.data.model.Job
import kotlinx.android.synthetic.main.item_job.view.*

class JobAdapter(private val items: List<Job>?, private val context: Context, private val listener: OnStatusClickListener) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    class JobViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.jobTitleTv
        private val done = view.doneCb
        private val menu = view.optionMenu

        fun bind(job: Job, position: Int, listener: OnStatusClickListener, context: Context) {
            title.text = job.title
            done.isChecked = job.done
            done.setOnCheckedChangeListener { _, isChecked ->
                listener.onStatusClicked(job, position, isChecked)
            }
            menu.setOnClickListener {
                listener.onMenuClicked(job, it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        return JobViewHolder(LayoutInflater.from(context).inflate(R.layout.item_job, parent, false))
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val mJob: Job = items!![position]
        holder.bind(mJob, position, listener, context)
    }

    override fun getItemCount(): Int {
        return items!!.size
    }
}