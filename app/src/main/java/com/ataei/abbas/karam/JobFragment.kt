package com.ataei.abbas.karam

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.jobs.JobViewModel
import com.ataei.abbas.karam.utils.DialogListener
import com.ataei.abbas.karam.utils.EditDialog
import com.ataei.abbas.karam.utils.JobAdapter
import com.ataei.abbas.karam.utils.OnStatusClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_job.*

@AndroidEntryPoint
class JobFragment : Fragment(), OnStatusClickListener, DialogListener {
    private val viewModel: JobViewModel by viewModels()
    private lateinit var adapter: JobAdapter
    private var isDone: Boolean = false
    private var jobId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobRec.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        setupObservers()
        addJobBtn.setOnClickListener {
            val j = Job(null, jobEt.text.toString(), false)
            viewModel.insertJob(j)
            jobEt.setText("")
        }
    }

    override fun onStatusClicked(job: Job, position: Int, isDone: Boolean) {
        val newJob = Job(job.id, job.title, isDone)
        viewModel.updateJob(newJob)
    }

    override fun onMenuClicked(job: Job, view: View) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val root = inflater.inflate(R.layout.custom_popup, null)
        val customPopup = PopupWindow(root,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            customPopup.elevation = 10.0F
        }
        val editBtn = root.findViewById<Button>(R.id.editJobBtn)
        editBtn.setOnClickListener {
            isDone = job.done
            jobId = job.id!!
            customPopup.dismiss()
            val dialog = EditDialog.newInstance(job.title)
            dialog.show(childFragmentManager, dialog.tag)
        }
        val removeBtn = root.findViewById<Button>(R.id.removeJobBtn)
        removeBtn.setOnClickListener {
            viewModel.deleteJob(job)
            customPopup.dismiss()
        }
        customPopup.isFocusable = true
        customPopup.setBackgroundDrawable(null)
        customPopup.showAsDropDown(view)
    }

    override fun onDismiss(newTitle: String) {
        val newJob = Job(jobId, newTitle, isDone)
        viewModel.updateJob(newJob)
    }

    private fun setupObservers() {
        viewModel.jobs.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                adapter = JobAdapter(it, requireContext(), this)
                jobRec.adapter = adapter
            }
        })
    }
}