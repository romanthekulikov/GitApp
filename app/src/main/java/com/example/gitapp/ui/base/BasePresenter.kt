package com.example.gitapp.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import moxy.MvpPresenter
import kotlin.coroutines.CoroutineContext

const val ERROR_NO_INTERNET = "No internet"
const val ERROR_EXCEEDED_LIMIT = "Limit is exceeded"
const val ERROR_NO_DATA = "This repository has no stars"
const val ERROR_GITHUB_IS_SHUTDOWN = "Github is shutdown"

open class BasePresenter<T : BaseView>(
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main
) : MvpPresenter<T>(), CoroutineScope {
    fun showError(message: String) {
        viewState.showError(message)
    }

    override fun onDestroy() {
        coroutineContext.cancelChildren()
        super.onDestroy()
    }
}