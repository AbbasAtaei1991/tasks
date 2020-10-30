package com.ataei.abbas.karam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.utils.JobAdapter
import com.ataei.abbas.karam.utils.OnStatusClickListener
import kotlinx.android.synthetic.main.fragment_job.*

class JobFragment : Fragment(), OnStatusClickListener {
    private var jobList: MutableList<Job> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobList.add(Job("نماز صبح", false))
        jobList.add(Job("دعای عهد", false))
        jobList.add(Job("دعای روز", false))
        jobList.add(Job("زیارت روز", false))
        jobList.add(Job("ذکر روز", false))
        jobList.add(Job("ورزش", false))
        jobList.add(Job("مطالعه", false))
        jobList.add(Job("تلاوت ۵۰ آیه قرآن", false))
        jobList.add(Job("خواندن تفسیر دو آیه", false))

        jobRec.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        jobRec.adapter = JobAdapter(jobList, requireContext(), this)
    }

    override fun onStatusClicked(job: Job, position: Int, isDone: Boolean) {
        Toast.makeText(requireContext(), job.title, Toast.LENGTH_SHORT).show()
    }
}