package com.example.gitapp.ui.base

import android.widget.Toast
import moxy.MvpAppCompatActivity

open class BaseActivity : MvpAppCompatActivity(), BaseView {
    override fun showError(errorMsg: String) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
    }
}