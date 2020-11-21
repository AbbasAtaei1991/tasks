package com.ataei.abbas.karam

import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_debt.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DebtFragment : Fragment(), OnStatusClickListener {
    private val viewModel: DeptViewModel by viewModels()
    private lateinit var adapter: MyAdapter
    private var parents: ArrayList<Parent> = ArrayList()
    private lateinit var mainChart: PieChart
    private var sum = 0
    private var done = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_debt, container, false)
        mainChart = view.findViewById(R.id.mainChart)
        return view
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
                getJobsListSize()
            }
        }
    }

    private fun getJobsListSize() {
        viewModel.getJobs().observe(viewLifecycleOwner, {
            sum = it.size
            observeDone()
        })
    }

    private fun observeDone() {
        viewModel.getJobsByStatus(true).observe(viewLifecycleOwner, {
            done = it.size
            val percent = (done.toFloat()/ sum.toFloat()) * 100
            initChart(mainChart, percent)
        })
    }

    private fun initChart(chart: PieChart, percent: Float) {
        val pieList = ArrayList<PieEntry>()
        val colorList = ArrayList<Int>()
        pieList.add(PieEntry(percent, ""))
        colorList.add(chart.context.resources.getColor(R.color.teal_200))
        pieList.add(PieEntry((100 - percent), ""))
        colorList.add(chart.context.resources.getColor(R.color.red))
        val pieDataSet = PieDataSet(pieList, "")
        pieDataSet.colors = colorList
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(false)
        chart.data = pieData
        with(chart) {
            isDrawHoleEnabled = true
            holeRadius = 75F
            description.isEnabled = false
            legend.isEnabled = false
            centerText = percent.toInt().toString()+"%"
            setCenterTextSize(10F)
            setEntryLabelColor(android.R.color.transparent)
            invalidate()
        }
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