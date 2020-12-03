package com.ataei.abbas.karam.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ataei.abbas.karam.databinding.DialogConfirmBinding
import java.lang.ClassCastException

class ConfirmDialog : DialogFragment() {
    private var listener: OnConfirmListener? = null
    private lateinit var binding: DialogConfirmBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as OnConfirmListener
        } catch (e : ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnYes.setOnClickListener {
            listener!!.onConfirm()
            dismiss()
        }
        binding.btnNo.setOnClickListener { dismiss() }
    }
}