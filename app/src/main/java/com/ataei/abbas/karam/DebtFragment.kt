package com.ataei.abbas.karam

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.jobs.JobViewModel
import com.ataei.abbas.karam.utils.DateUtils
import com.ataei.abbas.karam.utils.JobAdapter
import com.ataei.abbas.karam.utils.OnStatusClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_debt.*
import kotlinx.android.synthetic.main.fragment_job.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class DebtFragment : Fragment(), OnStatusClickListener {
    private val viewModel: JobViewModel by viewModels()
    private lateinit var adapter: JobAdapter
    private var jobList: MutableList<Job> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_debt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pendingRv.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        observeJobs()
    }

    private fun observeJobs() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getJobsByStatus(false).observe(viewLifecycleOwner, Observer {
                for (item in it) {
                    if (item.date != DateUtils.getTimeStampFromDate(Date())) {
                        jobList.add(item)
                    }
                }
                adapter = JobAdapter(jobList, requireContext(), this@DebtFragment)
                pendingRv.adapter = adapter
            })
        }
    }

    override fun onStatusClicked(job: Job, position: Int, isDone: Boolean) {
        val newJob = Job(job.id, job.title, job.ransom, isDone, job.date, job.repeat)
        Handler().postDelayed({
            viewModel.updateJob(newJob)
        }, 500)
    }

    override fun onMenuClicked(job: Job, view: View) {
    }

}