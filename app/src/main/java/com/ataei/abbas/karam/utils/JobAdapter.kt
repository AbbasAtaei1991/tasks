package com.ataei.abbas.karam.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.databinding.ItemJobBinding

class JobAdapter(private val items: List<Job>?, private val context: Context, private val listener: OnStatusClickListener) :
    ListAdapter<Job, JobAdapter.JobViewHolder>(DIFF_CALLBACK) {

    class JobViewHolder(val binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.jobTitleTv
        private val done = binding.doneCb
        private val menu = binding.optionMenu

        fun bind(job: Job, position: Int, listener: OnStatusClickListener) {
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
        val binding = ItemJobBinding.inflate(LayoutInflater.from(context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val mJob: Job = items!![position]
        holder.bind(mJob, position, listener)
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Job> =
            object : DiffUtil.ItemCallback<Job>() {
                override fun areItemsTheSame(oldItem: Job, newItem: Job) =
                    oldItem.title == newItem.title

                override fun areContentsTheSame(oldItem: Job, newItem: Job) =
                    oldItem.title == newItem.title
            }
    }

}