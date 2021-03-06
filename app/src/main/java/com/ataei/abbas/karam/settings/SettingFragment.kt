package com.ataei.abbas.karam.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.ataei.abbas.karam.R
import com.ataei.abbas.karam.data.model.Daily
import com.ataei.abbas.karam.databinding.FragmentSettingBinding
import com.ataei.abbas.karam.utils.DailyAdapter
import com.ataei.abbas.karam.utils.OnMenuClick
import com.ataei.abbas.karam.utils.UiMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class SettingFragment : Fragment(), OnMenuClick {
    private val viewModel: SettingViewModel by viewModels()
    private lateinit var binding: FragmentSettingBinding
    private lateinit var adapter: DailyAdapter
    private var isCollapsed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dailyRv.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.dailyBtn.setOnClickListener {
            if (binding.dailyEt.text!!.isNotEmpty()) {
                search(binding.dailyEt.text!!.trim().toString())
            }
        }
        getAll()
        binding.ransomBtn.setOnClickListener {
            binding.etRansom.isEnabled = true
            binding.etRansom.setText("")
        }
        binding.etRansom.setOnEditorActionListener { v, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                if (binding.etRansom.text!!.isNotEmpty()) {
                    viewModel.saveRansom(binding.etRansom.text.toString())
                    binding.etRansom.isEnabled = false
                }
            }
            false
        }
        observeRansom()
        binding.uiModeSw.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked) {
                true -> viewModel.saveUiMode(UiMode.DARK)
                false -> viewModel.saveUiMode(UiMode.LIGHT)
            }
        }
        binding.aboutAppCv.setOnClickListener {
            isCollapsed = if (isCollapsed) {
                expand(binding.aboutAppCv)
                binding.aboutAppRl.visibility = View.GONE
                binding.appDescTv.visibility = View.VISIBLE
                false
            } else {
                collapse(binding.aboutAppCv)
                binding.aboutAppRl.visibility = View.VISIBLE
                binding.appDescTv.visibility = View.GONE
                true
            }
        }
        initListeners()
    }

    private fun initListeners() {
        binding.instaIv.setOnClickListener {
            startActivity(desIntent("https://www.instagram.com/a__ataei"))
        }
        binding.telegramIv.setOnClickListener {
            startActivity(desIntent("https://telegram.me/a1b9b9a1s"))
        }
        binding.linkedInIv.setOnClickListener {
            startActivity(desIntent("https://www.linkedin.com/in/abbas-ataei-32a23415a/"))
        }
        binding.aparatIv.setOnClickListener {
            startActivity(desIntent("https://www.aparat.com/abbas_1991"))
        }
        binding.youtubeIv.setOnClickListener {
            startActivity(desIntent("https://www.youtube.com/channel/UCh0DIVIpqAltVkr3fgDFHqw"))
        }
    }

    private fun desIntent(uri: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    }

    private fun getAll() {
        viewModel.items.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter = DailyAdapter(it, requireContext(), this@SettingFragment)
                binding.dailyRv.adapter = adapter
            }
        })
    }

    private fun insert(title: String) {
        val daily = Daily(null, title)
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.insert(daily)
        }
        binding.dailyEt.setText("")
    }

    private fun search(s: String) {
        viewModel.searchDaily(s).observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                insert(s)
            } else {
                binding.dailyEt.setText("")
            }
        })
    }

    override fun onMenuClicked(daily: Daily, view: View) {
        lifecycleScope.launch(Dispatchers.IO) { viewModel.delete(daily) }
    }

    private fun observeRansom() {
        viewModel.savedRansom.observe(viewLifecycleOwner, {
            binding.etRansom.setText(it)
        })
    }

    private fun expand(view: ViewGroup) {
        TransitionManager.beginDelayedTransition(
            view, TransitionSet()
                .addTransition(ChangeBounds())
        )

        val params = view.layoutParams
        params.height = resources.getDimension(R.dimen.newHeight).roundToInt()
        view.layoutParams = params
    }

    private fun collapse(view: ViewGroup) {
        TransitionManager.beginDelayedTransition(
            view, TransitionSet()
                .addTransition(ChangeBounds())
        )

        val params = view.layoutParams
        params.height = resources.getDimension(R.dimen.oldHeight).roundToInt()
        view.layoutParams = params
    }

}