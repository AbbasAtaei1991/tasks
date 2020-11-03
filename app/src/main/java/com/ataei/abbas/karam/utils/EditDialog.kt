package com.ataei.abbas.karam.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.ataei.abbas.karam.R
import kotlinx.android.synthetic.main.dialog_edit.*
import java.lang.ClassCastException

class EditDialog : DialogFragment() {

    private var listener: DialogListener? = null
    private var done: Boolean = true
    private var oldTitle: String = ""
    private var oldRansom: String = ""

    companion object {
        private const val IS_DONE = "isDone"
        private const val TITLE = "title"
        private const val RANSOM = "ransom"

        fun newInstance(isDone: Boolean, title: String, ransom: String) = EditDialog().apply {
            arguments = bundleOf(
                IS_DONE to isDone,
                TITLE to title,
                RANSOM to ransom
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as DialogListener
        } catch (e : ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        done = requireArguments().getBoolean("isDone")
        oldTitle = requireArguments().getString("title")!!
        oldRansom = requireArguments().getString("ransom")!!
        return inflater.inflate(R.layout.dialog_edit, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        JobTitleEt.setText(oldTitle)
        ransomEt.setText(oldRansom)
        confirmBtn.setOnClickListener {
            if (JobTitleEt.text!!.isNotEmpty() && ransomEt.text!!.isNotEmpty()) {
                dismiss()
                listener?.onDismiss(JobTitleEt.text.toString(), ransomEt.text.toString(), done)
            } else {
                Toast.makeText(requireContext(), "موارد مورد نیاز را وارد کنید!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}