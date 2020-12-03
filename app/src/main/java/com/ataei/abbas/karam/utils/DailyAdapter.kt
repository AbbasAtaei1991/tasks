package com.ataei.abbas.karam.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ataei.abbas.karam.data.model.Daily
import com.ataei.abbas.karam.databinding.ItemDailyBinding

class DailyAdapter(private val items: List<Daily>, private val context: Context, private val listener: OnMenuClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    class DailyViewHolder(val binding: ItemDailyBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.titleTv
        private val menu = binding.optionMenuIv

        fun bind(daily: Daily, position: Int, listener: OnMenuClick) {
            title.text = daily.title
            menu.setOnClickListener {
                listener.onMenuClicked(daily, it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val d = items[position]
        (holder as DailyViewHolder).bind(d, position, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}