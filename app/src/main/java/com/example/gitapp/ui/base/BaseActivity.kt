package com.example.gitapp.ui.base

import com.example.gitapp.ui.dialogs.ErrorDialog
import moxy.MvpAppCompatActivity

const val TAG_ERROR_DIALOG = "error"
open class BaseActivity : MvpAppCompatActivity(), BaseView {

    override fun showError(errorMsg: String) {
         ErrorDialog.Builder()
            .setMessage(errorMsg)
            .build()
            .show(supportFragmentManager, TAG_ERROR_DIALOG)
    }
}