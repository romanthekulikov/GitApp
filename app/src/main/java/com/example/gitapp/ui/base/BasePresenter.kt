package com.example.gitapp.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import moxy.MvpPresenter
import kotlin.coroutines.CoroutineContext

open class BasePresenter<T : BaseView>(override val coroutineContext: CoroutineContext = Job()) : MvpPresenter<T>(),
    CoroutineScope {
    fun showError(message: String) {
        viewState.showError(message)
    }

    override fun onDestroy() {
        coroutineContext.cancelChildren()
        super.onDestroy()
    }
}