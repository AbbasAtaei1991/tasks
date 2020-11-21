package com.ataei.abbas.karam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ataei.abbas.karam.data.model.DayWithJobs
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.data.model.Parent
import com.ataei.abbas.karam.dept.DeptViewModel
import com.ataei.abbas.karam.utils.MyAdapter
import com.ataei.abbas.karam.utils.OnStatusClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_debt.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DebtFragment : Fragment(), OnStatusClickListener {
    private val viewModel: DeptViewModel by viewModels()
    private lateinit var adapter: MyAdapter
    private var parents: ArrayList<Parent> = ArrayList()

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
        getList()
    }

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
                getDept()
            }
        }
    }

    private fun getDept() {
        viewModel.getJobsByStatus(false).observe(viewLifecycleOwner, {
            var dept = 0
            for (job in it) {
                if (!job.pay)
                    dept += 1
            }
            tvDept.text = (dept * 1000).toString()
        })
    }

    override fun onStatusClicked(job: Job, position: Int, isDone: Boolean) {

    }

    override fun onMenuClicked(job: Job, view: View) {
    }

    override fun onPay(job: Job) {
        val newJob = Job(job.id, job.title, job.ransom, job.done, job.date, job.repeat, job.dayId, true, job.priority)
        viewModel.updateJob(newJob)
    }

}