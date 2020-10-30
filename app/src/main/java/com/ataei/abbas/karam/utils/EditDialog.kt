package com.ataei.abbas.karam.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.ataei.abbas.karam.R
import kotlinx.android.synthetic.main.dialog_edit.*
import java.lang.ClassCastException

class EditDialog : DialogFragment() {

    private var listener: DialogListener? = null
    private var oldTitle: String? = ""

    companion object {
        private const val TITLE = "title"

        fun newInstance(title: String) = EditDialog().apply {
            arguments = bundleOf(
                TITLE to title
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
        oldTitle = arguments?.getString("title")
        return inflater.inflate(R.layout.dialog_edit, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editJobEt.setText(oldTitle)
        confirmBtn.setOnClickListener {
            dismiss()
            listener?.onDismiss(editJobEt.text.toString())
        }
    }
}