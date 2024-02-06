package com.example.gitapp.ui.base

import com.example.gitapp.ui.dialogs.ErrorDialog
import moxy.MvpAppCompatActivity

open class BaseActivity : MvpAppCompatActivity(), BaseView {
    override fun showError(errorMsg: String) {
        val errorDialog = ErrorDialog.Builder()
            .setMessage(errorMsg)
            .build()
        errorDialog.show(supportFragmentManager, "")
    }
}