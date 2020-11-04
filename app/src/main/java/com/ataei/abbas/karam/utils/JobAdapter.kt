package com.ataei.abbas.karam.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ataei.abbas.karam.R
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.utils.Constants.Companion.VIEW_TYPE_JOB
import com.ataei.abbas.karam.utils.Constants.Companion.VIEW_TYPE_PENDING
import kotlinx.android.synthetic.main.item_job.view.*
import kotlinx.android.synthetic.main.item_job.view.doneCb
import kotlinx.android.synthetic.main.item_job.view.optionMenu
import kotlinx.android.synthetic.main.item_pending.view.*
import java.util.*

class JobAdapter(private val items: List<Job>?, private val context: Context, private val listener: OnStatusClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.pendingTitleTv
        private val date = view.pendingDateTv
        private val done = view.pendingCb
        private val menu = view.optionMenu

        fun bind(job: Job, position: Int, listener: OnStatusClickListener, context: Context) {
            title.text = job.title
            date.text = job.date
            done.isChecked = job.done
            done.setOnCheckedChangeListener { _, isChecked ->
                listener.onStatusClicked(job, position, isChecked)
            }
            menu.setOnClickListener {
                listener.onMenuClicked(job, it)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val job = this.items?.get(position)
        return if (job?.date == DateUtils.getTimeStampFromDate(Date())) {
            VIEW_TYPE_JOB
        } else {
            VIEW_TYPE_PENDING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        if (viewType == VIEW_TYPE_JOB) {
            view = LayoutInflater.from(context).inflate(R.layout.item_job, parent, false)
            return JobViewHolder(view)
        } else if (viewType == VIEW_TYPE_PENDING) {
            view = LayoutInflater.from(context).inflate(R.layout.item_pending, parent, false)
            return MyViewHolder(view)
        }
        return JobViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mJob: Job = items!![position]
        when(holder.itemViewType) {
            VIEW_TYPE_JOB -> {
                (holder as JobViewHolder).bind(mJob, position, listener, context)
            }
            VIEW_TYPE_PENDING -> {
                (holder as MyViewHolder).bind(mJob, position, listener, context)
            }
        }
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

}