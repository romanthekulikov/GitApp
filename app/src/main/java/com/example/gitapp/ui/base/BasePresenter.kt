package com.example.gitapp.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import moxy.MvpPresenter
import kotlin.coroutines.CoroutineContext
const val ERROR_NO_INTERNET = "No internet"
const val ERROR_TIMED_OUT = "Timed out"
const val ERROR_GITHUB_IS_SHUTDOWN = "Github is shutdown"
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