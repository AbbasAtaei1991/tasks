package com.ataei.abbas.karam

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
//        CoroutineScope(Dispatchers.IO).launch { getDay() }
        observeJobs()
    }

    private suspend fun getDay() {
        withContext(Dispatchers.IO) {
            val mList: List<Job> = viewModel.getDays()[0].jobs
            adapter = JobAdapter(mList, requireContext(), this@DebtFragment)
            pendingRv.adapter = adapter

        }
    }

    private fun observeJobs() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getJobsByStatus(false).observe(viewLifecycleOwner, Observer {
                for (item in it) {
                    if (item.date != DateUtils.getTimeStampFromDate(Date())) {
                        jobList.add(item)
                    }
                }
                adapter = JobAdapter(it, requireContext(), this@DebtFragment)
                pendingRv.adapter = adapter
            })
        }
    }

    override fun onStatusClicked(job: Job, position: Int, isDone: Boolean) {
        val newJob = Job(job.id, job.title, job.ransom, isDone, job.date, job.repeat, job.dayId)
        Handler().postDelayed({
            viewModel.updateJob(newJob)
        }, 500)
    }

    override fun onMenuClicked(job: Job, view: View) {
    }

}