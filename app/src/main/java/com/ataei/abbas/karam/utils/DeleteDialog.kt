package com.ataei.abbas.karam.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ataei.abbas.karam.databinding.DialogDeleteBinding
import java.lang.ClassCastException

class DeleteDialog : DialogFragment() {
    private var listener: OnDeleteListener? = null
    private lateinit var binding: DialogDeleteBinding
    private var flag = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as OnDeleteListener
        } catch (e : ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogDeleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.yesBtn.setOnClickListener {
            flag = true
            listener!!.onDelete()
            dismiss()
        }
        binding.noBtn.setOnClickListener {
            listener!!.onNotDelete()
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!flag)
            listener!!.onNotDelete()
    }
}