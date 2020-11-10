package com.ataei.abbas.karam

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ataei.abbas.karam.data.model.DayWithJobs
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.data.model.Parent
import com.ataei.abbas.karam.dept.DeptViewModel
import com.ataei.abbas.karam.jobs.JobViewModel
import com.ataei.abbas.karam.utils.DateUtils
import com.ataei.abbas.karam.utils.JobAdapter
import com.ataei.abbas.karam.utils.MyAdapter
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
    private val viewModel: DeptViewModel by viewModels()
    private lateinit var adapter: MyAdapter
    private var jobList: MutableList<Job> = ArrayList()
    private var parents: ArrayList<Parent> = ArrayList()

    var p = 0

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
//        observeJobs()
//        getDay()
        getList()
    }


//    private fun getDay() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val mList: List<DayWithJobs> = viewModel.getDays()
//            if (!mList.isNullOrEmpty()) {
//                for (item in mList) {
//                    val parent = Parent(item.day, item.jobs)
//                    parents.add(parent)
//                }
//            }
//
//            launch(Dispatchers.Main) {
//                adapter = MyAdapter(parents, this@DebtFragment)
//                pendingRv.adapter = adapter
//            }
//
//        }
//    }

    private fun getList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val mList: List<DayWithJobs> = viewModel.getDays()
            for (item in mList) {
                val parent = Parent(item.day, item.jobs)
                parents.add(parent)
            }

            launch(Dispatchers.Main) {
                adapter = MyAdapter(parents, this@DebtFragment)
                pendingRv.adapter = adapter
            }
        }
    }

/*
    private fun observeJobs() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getJobsByStatus(false).observe(viewLifecycleOwner, Observer {
                for (item in it) {
                    if (item.date != DateUtils.getTimeStampFromDate(Date())) {
                        jobList.add(item)
                    }
                }
//                adapter = JobAdapter(it, requireContext(), this@DebtFragment)
//                pendingRv.adapter = adapter
            })
        }
    }
*/

    override fun onStatusClicked(job: Job, position: Int, isDone: Boolean) {
        val newJob = Job(job.id, job.title, job.ransom, isDone, job.date, job.repeat, job.dayId, false)
        Handler().postDelayed({
//            viewModel.updateJob(newJob)
        }, 500)
    }

    override fun onMenuClicked(job: Job, view: View) {
    }

    override fun onPay(job: Job) {
        val newJob = Job(job.id, job.title, job.ransom, job.done, job.date, job.repeat, job.dayId, true)
        viewModel.updateJob(newJob)
    }

}