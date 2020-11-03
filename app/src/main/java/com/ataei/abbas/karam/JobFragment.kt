package com.ataei.abbas.karam

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.jobs.JobViewModel
import com.ataei.abbas.karam.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_job.*
import java.util.*


@AndroidEntryPoint
class JobFragment : Fragment(), OnStatusClickListener, DialogListener {
    private val viewModel: JobViewModel by viewModels()
    private lateinit var adapter: JobAdapter
    private var isDone: Boolean = false
    private var jobId: Int = 0
    private var editTextWidth: Int = 0
    private var buttonWidth: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobCl.post {
            editTextWidth = mItemInputEditText.width
            buttonWidth = addJobBtn.width
            addJobBtn.setOnClickListener {
                showEditText()
            }

            mItemInputEditText.setOnEditorActionListener { v, actionId, event ->
                if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (mItemInputEditText.text!!.isNotEmpty()) {
                        val newJob = Job(null, mItemInputEditText.text.toString(), 1000, false)
                        viewModel.insertJob(newJob)
                        mItemInputEditText.setText("")
                        Handler().postDelayed({
                            hideEditText()
                        }, 300)
                    }

                }
                false
            }
        }
        jobRec.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        setupObservers()
        showFab()
        dateTv.text = DateUtils.getShamsiDate(Date())

    }

    private fun showEditText() {
        val from: Int = addJobBtn.width
        val to = (from * 3.0f).toInt() // increase by 20%

        val interpolator = LinearInterpolator()

        val firstAnimator = ValueAnimator.ofInt(from, to)
        firstAnimator.setTarget(addJobBtn)
        firstAnimator.interpolator = interpolator
        firstAnimator.duration = 200

        val params: ViewGroup.LayoutParams = addJobBtn.layoutParams
        firstAnimator.addUpdateListener { animation ->
            params.width = (animation.animatedValue as Int)
            addJobBtn.alpha = 1 - animation.animatedFraction
            addJobBtn.requestLayout()
        }

        firstAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                addJobBtn.alpha = 1.0f
                addJobBtn.visibility = View.GONE
                mItemInputEditText.visibility = View.VISIBLE
                val secondAnimator = ValueAnimator.ofInt(to, editTextWidth)
                secondAnimator.setTarget(mItemInputEditText)
                secondAnimator.interpolator = interpolator
                secondAnimator.duration = 200
                val params: ViewGroup.LayoutParams = mItemInputEditText.layoutParams
                secondAnimator.addUpdateListener { animation ->
                    params.width = (animation.animatedValue as Int)
                    mItemInputEditText.requestLayout()
                }
                secondAnimator.start()
            }
        })

        firstAnimator.start()
    }

    private fun hideEditText() {
        val from: Int = mItemInputEditText.width
        val to = (from * 0.8f).toInt() // increase by 20%

        val interpolator = LinearInterpolator()

        val firstAnimator = ValueAnimator.ofInt(from, to)
        firstAnimator.setTarget(mItemInputEditText)
        firstAnimator.interpolator = interpolator
        firstAnimator.duration = 150

        val params: ViewGroup.LayoutParams = mItemInputEditText.layoutParams
        firstAnimator.addUpdateListener { animation ->
            params.width = (animation.animatedValue as Int)
            mItemInputEditText.requestLayout()
        }

        firstAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mItemInputEditText.visibility = View.GONE
                addJobBtn.visibility = View.VISIBLE
                val secondAnimator = ValueAnimator.ofInt(to, buttonWidth)
                secondAnimator.setTarget(addJobBtn)
                secondAnimator.interpolator = interpolator
                secondAnimator.duration = 150
                val params: ViewGroup.LayoutParams = addJobBtn.layoutParams
                secondAnimator.addUpdateListener { animation ->
                    params.width = (animation.animatedValue as Int)
                    addJobBtn.alpha = animation.animatedFraction
                    addJobBtn.requestLayout()
                }
                secondAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        addJobBtn.alpha = 0.0f
                    }
                })
                secondAnimator.start()
            }
        })

        firstAnimator.start()
    }

    override fun onStatusClicked(job: Job, position: Int, isDone: Boolean) {
        val newJob = Job(job.id, job.title, job.ransom, isDone)
        Handler().postDelayed({
            viewModel.updateJob(newJob)
        }, 500)
    }

    override fun onMenuClicked(job: Job, view: View) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val root = inflater.inflate(R.layout.custom_popup, null)
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
            isDone = job.done
            jobId = job.id!!
            customPopup.dismiss()
            val dialog = EditDialog.newInstance(job.done, job.title, job.ransom.toString())
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

    private fun setupObservers() {
        viewModel.jobs.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter = JobAdapter(it, requireContext(), this)
                jobRec.adapter = adapter
            }
        })
    }

    override fun onDismiss(title: String, ransom: String, done: Boolean) {
        val mJob = Job(jobId, title, ransom.toInt(), done)
        viewModel.updateJob(mJob)
    }

    private fun showFab() {
        jobRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (addJobBtn.isShown)
                        addJobBtn.visibility = View.INVISIBLE
                } else if (dy < 0) {
                    if (!addJobBtn.isShown)
                        addJobBtn.visibility = View.VISIBLE
                }
            }
        })
    }
}