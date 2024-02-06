package com.example.gitapp.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gitapp.databinding.DialogErrorBinding

class ErrorDialog private constructor(
    private val message: String?,
    private val action: () -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogErrorBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogErrorBinding.inflate(layoutInflater)
        binding.buttonOk.setOnClickListener {
            action()
            dismiss()
        }
        binding.textErrorMessage.text = message
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // make rounded dialog

        return binding.root
    }

    data class Builder(private var message: String? = null, private var action: () -> Unit = {}) {
        fun setMessage(message: String) = apply { this.message = message }
        fun setAction(action: () -> Unit) = apply { this.action = action }
        fun build() = ErrorDialog(message, action)
    }
}