package com.ataei.abbas.karam.utils

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ataei.abbas.karam.R
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.data.model.Parent
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.child_row.view.*
import kotlinx.android.synthetic.main.parent_row.view.*


class MyAdapter(parents: ArrayList<Parent>, private val listener: OnStatusClickListener) :
    ExpandableRecyclerViewAdapter<Job, Parent, MyAdapter.PViewHolder, MyAdapter.CViewHolder>(
        parents, ExpandingDirection.VERTICAL
    ) {

    override fun onCreateParentViewHolder(parent: ViewGroup, viewType: Int): PViewHolder {
        return PViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.parent_row,
                parent,
                false
            )
        )
    }

    override fun onBindParentViewHolder(
        parentViewHolder: PViewHolder,
        expandableType: Parent,
        position: Int
    ) {
        val date = DateUtils.getDateFromString(expandableType.day.date)
        parentViewHolder.containerView.tvDate.text = DateUtils.getShamsi(date)
        var d = 0
        for (job in expandableType.jobs) {
            if (job.done){
                d += 1
            }
        }
        val p: Double = (d.toDouble()/expandableType.jobs.size.toDouble()) * 100
        val pieChart = parentViewHolder.containerView.tvPercent
        initChart(pieChart, p.toFloat())
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
            holeRadius = 70F
            description.isEnabled = false
            legend.isEnabled = false
            centerText = percent.toInt().toString()
            setCenterTextSize(7F)
            setEntryLabelColor(android.R.color.transparent)
        }
    }

    override fun onCreateChildViewHolder(child: ViewGroup, viewType: Int): CViewHolder {
        return CViewHolder(
            LayoutInflater.from(child.context).inflate(
                R.layout.child_row,
                child,
                false
            )
        )
    }

    override fun onBindChildViewHolder(
        childViewHolder: CViewHolder,
        expandedType: Job,
        expandableType: Parent,
        position: Int
    ) {
        val title: TextView = childViewHolder.containerView.childTitleTv
        val ran: TextView = childViewHolder.containerView.ransomTv
        title.text = expandedType.title
        ran.text = expandedType.ransom
        if (expandedType.done) {
            childViewHolder.containerView.payIv.visibility = View.VISIBLE
            ran.visibility = View.INVISIBLE
        } else {
            childViewHolder.containerView.unDoneIv.visibility = View.VISIBLE
            if (expandedType.pay) {
                ran.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

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
        val tv: TextView = expandedViewHolder.containerView.ransomTv
        if (!expandedType.pay && !expandedType.done) {
            tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            listener.onPay(expandedType)
        }
    }
    
    class PViewHolder(v: View) : ExpandableRecyclerViewAdapter.ExpandableViewHolder(v)

    class CViewHolder(v: View) : ExpandableRecyclerViewAdapter.ExpandedViewHolder(v)

    override fun onStateChange(expandableViewHolder: PViewHolder, expandableType: Parent) {
        val arrow: ImageView = expandableViewHolder.containerView.chevronIv
//        val rotate = AnimationUtils.loadAnimation(expandableViewHolder.containerView.context, R.anim.rotate_clk)
//        arrow.startAnimation(rotate)
//        if (expandableType.isExpanded) {
//            arrow.setImageResource(R.drawable.chevron_up)
//        } else {
//            arrow.setImageResource(R.drawable.chevron_down)
//        }
    }
}