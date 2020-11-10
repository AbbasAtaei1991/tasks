package com.ataei.abbas.karam.utils

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ataei.abbas.karam.R
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.data.model.Parent
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
        parentViewHolder.containerView.tvPercent.text = p.toInt().toString()
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
        title.text = expandedType.title
        if (expandedType.done) {
            childViewHolder.containerView.payIv.visibility = View.VISIBLE
        } else {
            childViewHolder.containerView.unDoneIv.visibility = View.VISIBLE
            if (!expandedType.pay) {
                childViewHolder.containerView.doneCb.visibility = View.VISIBLE
            } else {
                title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
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
        val tv: TextView = expandedViewHolder.containerView.childTitleTv
        if (!expandedType.pay && !expandedType.done) {
            expandedViewHolder.containerView.doneCb.visibility = View.INVISIBLE
            tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            listener.onPay(expandedType)
        }
    }
    
    class PViewHolder(v: View) : ExpandableRecyclerViewAdapter.ExpandableViewHolder(v)

    class CViewHolder(v: View) : ExpandableRecyclerViewAdapter.ExpandedViewHolder(v)

    override fun onStateChange(expandableViewHolder: PViewHolder, expandableType: Parent) {
        val arrow: ImageView = expandableViewHolder.containerView.chevronIv
        val rotate = AnimationUtils.loadAnimation(expandableViewHolder.containerView.context, R.anim.rotate_clk)
        arrow.startAnimation(rotate)
        if (expandableType.isExpanded) {
//            arrow.setImageResource(R.drawable.chevron_up)
        } else {
//            arrow.setImageResource(R.drawable.chevron_down)
        }
    }

    private fun rotate(context: Context) {

    }
}