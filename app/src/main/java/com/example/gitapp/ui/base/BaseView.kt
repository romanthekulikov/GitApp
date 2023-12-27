package com.example.gitapp.ui.base

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface BaseView : MvpView {
    @StateStrategyType(value = SkipStrategy::class)
    fun showError(errorMsg: String)
}