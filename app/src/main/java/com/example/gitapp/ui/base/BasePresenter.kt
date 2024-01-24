package com.example.gitapp.ui.base

import moxy.MvpPresenter

open class BasePresenter<T : BaseView> : MvpPresenter<T>() {
    fun showError(message: String) {
        viewState.showError(message)
    }
}