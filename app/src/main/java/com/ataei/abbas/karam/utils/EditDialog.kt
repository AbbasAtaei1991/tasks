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
import com.ataei.abbas.karam.databinding.DialogEditBinding
import java.lang.ClassCastException

class EditDialog : DialogFragment() {

    private lateinit var binding: DialogEditBinding
    private var listener: DialogListener? = null
    private var repeat: Boolean = true
    private var oldTitle: String = ""
    private var oldRansom: String = ""

    companion object {
        private const val REPEAT = "repeat"
        private const val TITLE = "title"
        private const val RANSOM = "ransom"

        fun newInstance(repeat: Boolean, title: String, ransom: String) = EditDialog().apply {
            arguments = bundleOf(
                REPEAT to repeat,
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
        binding = DialogEditBinding.inflate(inflater, container, false)
        repeat = requireArguments().getBoolean("repeat")
        oldTitle = requireArguments().getString("title")!!
        oldRansom = requireArguments().getString("ransom")!!
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.JobTitleEt.setText(oldTitle)
        binding.ransomEt.setText(oldRansom)
        binding.repeatCb.isChecked = repeat
        binding.confirmBtn.setOnClickListener {
            if (binding.JobTitleEt.text!!.isNotEmpty() && binding.ransomEt.text!!.isNotEmpty()) {
                dismiss()
                listener?.onDismiss(binding.JobTitleEt.text.toString(), binding.ransomEt.text.toString(), binding.repeatCb.isChecked)
            } else {
                Toast.makeText(requireContext(), "موارد مورد نیاز را وارد کنید!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}