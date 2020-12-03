package com.ataei.abbas.karam.utils

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ataei.abbas.karam.R
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.data.model.Parent
import com.ataei.abbas.karam.databinding.ChildRowBinding
import com.ataei.abbas.karam.databinding.ParentRowBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class MyAdapter(parents: ArrayList<Parent>, private val listener: OnStatusClickListener) :
    ExpandableRecyclerViewAdapter<Job, Parent, MyAdapter.PViewHolder, MyAdapter.CViewHolder>(
        parents, ExpandingDirection.VERTICAL
    ) {

    override fun onCreateParentViewHolder(parent: ViewGroup, viewType: Int): PViewHolder {
        val binding = ParentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PViewHolder(binding)
    }

    override fun onBindParentViewHolder(
        parentViewHolder: PViewHolder,
        expandableType: Parent,
        position: Int
    ) {
        parentViewHolder.bind(expandableType)
    }

    override fun onCreateChildViewHolder(child: ViewGroup, viewType: Int): CViewHolder {
        val binding = ChildRowBinding.inflate(LayoutInflater.from(child.context), child, false)
        return CViewHolder(binding)
    }

    override fun onBindChildViewHolder(
        childViewHolder: CViewHolder,
        expandedType: Job,
        expandableType: Parent,
        position: Int
    ) {
        childViewHolder.bind(expandedType)
    }

    override fun onExpandableClick(expandableViewHolder: PViewHolder, expandableType: Parent) {
//        if (expandableType.isExpanded) {
//            expandableViewHolder.containerView.chevronIv.setImageResource(R.drawable.chevron_down)
//        } else {
//            expandableViewHolder.containerView.chevronIv.setImageResource(R.drawable.chevron_up)
//        }
    }

    override fun onExpandedClick(
        expandableViewHolder: PViewHolder,
        expandedViewHolder: CViewHolder,
        expandedType: Job,
        expandableType: Parent
    ) {
        expandedViewHolder.onChildClick(expandedType, listener)
    }
    
    class PViewHolder(private val binding: ParentRowBinding) : ExpandableRecyclerViewAdapter.ExpandableViewHolder(binding.root) {
        fun bind(parent: Parent) {
            val date = DateUtils.getDateFromString(parent.day.date)
            binding.tvDate.text = DateUtils.getShamsi(date)
            if (parent.jobs.isNotEmpty()) {
                binding.chevronIv.visibility = View.VISIBLE
            }
            var d = 0
            for (job in parent.jobs) {
                if (job.done){
                    d += 1
                }
            }
            val p: Double = (d.toDouble()/parent.jobs.size.toDouble()) * 100
            val pieChart = binding.tvPercent
            initChart(pieChart, p.toFloat())
        }

        private fun initChart(chart: PieChart, percent: Float) {
            val pieList = ArrayList<PieEntry>()
            val colorList = ArrayList<Int>()
            pieList.add(PieEntry(percent, ""))
            colorList.add(ContextCompat.getColor(chart.context, R.color.teal_200))
            pieList.add(PieEntry((100 - percent), ""))
            colorList.add(ContextCompat.getColor(chart.context, R.color.light_red))
            val pieDataSet = PieDataSet(pieList, "")
            pieDataSet.colors = colorList
            val pieData = PieData(pieDataSet)
            pieData.setDrawValues(false)
            chart.data = pieData
            with(chart) {
                isDrawHoleEnabled = true
                holeRadius = 70F
                description.isEnabled = false
                legend.isEnabled = false
                centerText = percent.toInt().toString()
                setCenterTextSize(7F)
                setEntryLabelColor(android.R.color.transparent)
            }
        }
    }

    class CViewHolder(private val binding: ChildRowBinding) : ExpandableRecyclerViewAdapter.ExpandedViewHolder(binding.root) {
        fun bind(job: Job) {
            val title: TextView = binding.childTitleTv
            val ran: TextView = binding.ransomTv
            title.text = job.title
            ran.text = job.ransom
            if (job.done) {
                binding.payIv.visibility = View.VISIBLE
                ran.visibility = View.INVISIBLE
            } else {
                binding.unDoneIv.visibility = View.VISIBLE
                if (job.pay) {
                    ran.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }
        fun onChildClick(job: Job, listener: OnStatusClickListener) {
            val tv: TextView = binding.ransomTv
            if (!job.pay && !job.done) {
                tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                listener.onPay(job)
            }
        }
    }

    override fun onStateChange(expandableViewHolder: PViewHolder, expandableType: Parent) {
//        val rotate = AnimationUtils.loadAnimation(expandableViewHolder.containerView.context, R.anim.rotate_clk)
//        GlobalScope.launch(Dispatchers.Main) {
//            val arrowDown: ImageView = expandableViewHolder.containerView.chevronIv
//            val arrowUp: ImageView = expandableViewHolder.containerView.chevronUpIv
//            if (expandableType.isExpanded) {
//                arrowDown.visibility = View.INVISIBLE
//                arrowUp.visibility = View.VISIBLE
//            } else {
//                arrowDown.visibility = View.VISIBLE
//                arrowUp.visibility = View.INVISIBLE
//            }
//        }
    }
}