package com.ataei.abbas.karam.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ataei.abbas.karam.R
import com.ataei.abbas.karam.data.model.Daily
import kotlinx.android.synthetic.main.item_daily.view.*

class DailyAdapter(private val items: List<Daily>, private val context: Context, private val listener: OnMenuClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    class DailyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.titleTv
        private val menu = view.optionMenuIv

        fun bind(daily: Daily, position: Int, listener: OnMenuClick) {
            title.text = daily.title
            menu.setOnClickListener {
                listener.onMenuClicked(daily, it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_daily, parent, false)
        return DailyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val d = items[position]
        (holder as DailyViewHolder).bind(d, position, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}