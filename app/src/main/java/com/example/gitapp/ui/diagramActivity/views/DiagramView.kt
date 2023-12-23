package com.example.gitapp.ui.diagramActivity.views

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface DiagramView : MvpView {
    fun showError(errorMessage: String)
    fun changeVisibilityProgressBar(visibility: Int)
}