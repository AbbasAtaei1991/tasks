package com.ataei.abbas.karam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ataei.abbas.karam.data.model.Daily
import com.ataei.abbas.karam.settings.DailyViewModel
import com.ataei.abbas.karam.utils.DailyAdapter
import com.ataei.abbas.karam.utils.OnMenuClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : Fragment(), OnMenuClick {
    private val viewModel: DailyViewModel by viewModels()
    private lateinit var adapter: DailyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dailyRv.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        dailyBtn.setOnClickListener {
            if (dailyEt.text!!.isNotEmpty()) {
                search(dailyEt.text!!.trim().toString())
            }
        }
        getAll()
    }

    private fun getAll() {
        viewModel.items.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter = DailyAdapter(it, requireContext(), this@SettingFragment)
                dailyRv.adapter = adapter
            }
        })
    }

    private fun insert(title: String) {
        val daily = Daily(null, title)
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.insert(daily)
        }
        dailyEt.setText("")
    }

    private fun search(s: String) {
        viewModel.searchDaily(s).observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                insert(s)
            } else {
                dailyEt.setText("")
            }
        })
    }

    override fun onMenuClicked(daily: Daily, view: View) {
        lifecycleScope.launch(Dispatchers.IO) { viewModel.delete(daily) }
    }

}