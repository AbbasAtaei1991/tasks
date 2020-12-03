package com.ataei.abbas.karam.jobs

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ataei.abbas.karam.R
import com.ataei.abbas.karam.data.model.Daily
import com.ataei.abbas.karam.data.model.Day
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.databinding.FragmentJobBinding
import com.ataei.abbas.karam.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class JobFragment : Fragment(), OnStatusClickListener, DialogListener, OnConfirmListener {
    private val viewModel: JobViewModel by viewModels()
    private lateinit var binding: FragmentJobBinding
    private lateinit var adapter: JobAdapter
    private lateinit var mInflater: LayoutInflater
    private var dailyList: MutableList<Daily> = ArrayList()
    private var complete: Boolean = false
    private var show: Boolean = true
    private var jobId: Int = 0
    private var editTextWidth: Int = 0
    private var buttonWidth: Int = 0
    private var currentDate: String = ""
    private var ransom: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobBinding.inflate(inflater, container, false)
        mInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        currentDate = DateUtils.getTimeStampFromDate(Date())
        insertDay(currentDate)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.jobCl.post {
            editTextWidth = binding.mItemInputEditText.width
            buttonWidth = binding.addJobBtn.width
            binding.addJobBtn.setOnClickListener {
                showEditText()
            }

            binding.mItemInputEditText.setOnEditorActionListener { _, actionId, event ->
                if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (binding.mItemInputEditText.text!!.isNotEmpty()) {
                        val newJob = Job(null, binding.mItemInputEditText.text.toString(), ransom, false, currentDate, true, currentDate, false, 1)
                        viewModel.insertJob(newJob)
                        binding.mItemInputEditText.setText("")
                        lifecycleScope.launch {
                            delay(300L)
                            hideEditText()
                        }
                    }
                }
                false
            }
        }
        binding.jobRec.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        showHideBtn()
        binding.dateTv.text = DateUtils.getShamsiDate(Date())
        observeRansom()
        binding.deleteMenu.setOnClickListener { onOptionsItemSelected() }
    }

    override fun onResume() {
        super.onResume()
        getJobsByDate(currentDate)
    }

    private fun onOptionsItemSelected() {
        val root = mInflater.inflate(R.layout.delet_menu, null)
        val customPopup = PopupWindow(
            root,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            customPopup.elevation = 10.0F
        }
        val removeBtn = root.findViewById<TextView>(R.id.removeAllTv)
        removeBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                show = false
                viewModel.clear(currentDate)
            }
            customPopup.dismiss()
        }
        val addDailiesBtn = root.findViewById<TextView>(R.id.insertDailiesTv)
        addDailiesBtn.setOnClickListener {
            getDailies()
            customPopup.dismiss()
        }
        customPopup.isFocusable = true
        customPopup.setBackgroundDrawable(null)
        customPopup.showAsDropDown(binding.deleteMenu)
    }

    private fun insertDay(date: String) {
        val day = Day(date, 0, false)
        viewModel.insertDay(day)
    }

    private fun showEditText() {
        val from: Int = binding.addJobBtn.width
        val to = (from * 3.0f).toInt()

        val interpolator = LinearInterpolator()

        val firstAnimator = ValueAnimator.ofInt(from, to)
        firstAnimator.setTarget(binding.addJobBtn)
        firstAnimator.interpolator = interpolator
        firstAnimator.duration = 200

        val params: ViewGroup.LayoutParams = binding.addJobBtn.layoutParams
        firstAnimator.addUpdateListener { animation ->
            params.width = (animation.animatedValue as Int)
            binding.addJobBtn.alpha = 1 - animation.animatedFraction
            binding.addJobBtn.requestLayout()
        }

        firstAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.addJobBtn.alpha = 1.0f
                binding.addJobBtn.visibility = View.GONE
                binding.mItemInputEditText.visibility = View.VISIBLE
                val secondAnimator = ValueAnimator.ofInt(to, editTextWidth)
                secondAnimator.setTarget(binding.mItemInputEditText)
                secondAnimator.interpolator = interpolator
                secondAnimator.duration = 200
                val params: ViewGroup.LayoutParams = binding.mItemInputEditText.layoutParams
                secondAnimator.addUpdateListener { animation ->
                    params.width = (animation.animatedValue as Int)
                    binding.mItemInputEditText.requestLayout()
                }
                secondAnimator.start()
            }
        })

        firstAnimator.start()
    }

    private fun hideEditText() {
        val from: Int = binding.mItemInputEditText.width
        val to = (from * 0.8f).toInt()

        val interpolator = LinearInterpolator()

        val firstAnimator = ValueAnimator.ofInt(from, to)
        firstAnimator.setTarget(binding.mItemInputEditText)
        firstAnimator.interpolator = interpolator
        firstAnimator.duration = 150

        val params: ViewGroup.LayoutParams = binding.mItemInputEditText.layoutParams
        firstAnimator.addUpdateListener { animation ->
            params.width = (animation.animatedValue as Int)
            binding.mItemInputEditText.requestLayout()
        }

        firstAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.mItemInputEditText.visibility = View.GONE
                binding.addJobBtn.visibility = View.VISIBLE
                val secondAnimator = ValueAnimator.ofInt(to, buttonWidth)
                secondAnimator.setTarget(binding.addJobBtn)
                secondAnimator.interpolator = interpolator
                secondAnimator.duration = 150
                val params: ViewGroup.LayoutParams = binding.addJobBtn.layoutParams
                secondAnimator.addUpdateListener { animation ->
                    params.width = (animation.animatedValue as Int)
                    binding.addJobBtn.alpha = animation.animatedFraction
                    binding.addJobBtn.requestLayout()
                }
                secondAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        binding.addJobBtn.alpha = 0.0f
                    }
                })
                secondAnimator.start()
            }
        })

        firstAnimator.start()
    }

    override fun onStatusClicked(job: Job, position: Int, isDone: Boolean) {
        val newJob = Job(job.id, job.title, job.ransom, isDone, job.date, job.repeat, job.dayId, false, 1)
        lifecycleScope.launch {
            delay(500L)
            viewModel.updateJob(newJob)
        }
    }

    override fun onMenuClicked(job: Job, view: View) {
        val root = mInflater.inflate(R.layout.custom_popup, null)
        val customPopup = PopupWindow(
            root,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            customPopup.elevation = 10.0F
        }
        val editBtn = root.findViewById<TextView>(R.id.editJobTv)
        editBtn.setOnClickListener {
            complete = job.done
            jobId = job.id!!
            customPopup.dismiss()
            val dialog = EditDialog.newInstance(job.repeat, job.title, job.ransom)
            dialog.show(childFragmentManager, dialog.tag)
        }
        val removeBtn = root.findViewById<TextView>(R.id.removeJobTv)
        removeBtn.setOnClickListener {
            viewModel.deleteJob(job)
            customPopup.dismiss()
        }
        customPopup.isFocusable = true
        customPopup.setBackgroundDrawable(null)
        customPopup.showAsDropDown(view)
    }

    override fun onPay(job: Job) {

    }

    private fun getJobsByDate(date: String) {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getJobsByDate(date).observe(viewLifecycleOwner, {
                if (it != null) {
                    adapter = JobAdapter(it, requireContext(), this@JobFragment)
                    binding.jobRec.adapter = adapter
                    if (it.isEmpty()) {
                        hasDaily()
                    }
                }
            })
        }
    }

    override fun onDismiss(title: String, ransom: String, repeat: Boolean) {
        val mJob = Job(jobId, title, ransom, complete, currentDate, repeat, currentDate, false, 1)
        viewModel.updateJob(mJob)
    }

    private fun showHideBtn() {
        binding.jobRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (binding.addJobBtn.isShown) {
                        binding.addJobBtn.visibility = View.INVISIBLE
                    }

                } else if (dy < 0) {
                    if (!binding.addJobBtn.isShown && !binding.mItemInputEditText.isVisible)
                        binding.addJobBtn.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun getDailies() {
        viewModel.items.observe(viewLifecycleOwner, {
            if (it != null) {
                for (item in it) {
                    dailyList.add(item)
                }
                insertNewList(dailyList)
            }
        })
    }

    private fun hasDaily() {
        viewModel.items.observe(viewLifecycleOwner, {
            if (it.isNotEmpty() && show) {
                val dialog = ConfirmDialog()
                dialog.show(childFragmentManager, dialog.tag)
            }
        })
    }

    private fun insertNewList(list: List<Daily>) {
        lifecycleScope.launch(Dispatchers.IO) {
            for (item in list) {
                val job = Job(null, item.title, ransom, false, currentDate, true, currentDate, false, 1)
                viewModel.insertJob(job)
            }
        }
    }

    override fun onConfirm() {
        getDailies()
    }

    private fun observeRansom() {
        viewModel.savedRansom.observe(viewLifecycleOwner, {
            ransom = it
        })
    }
}