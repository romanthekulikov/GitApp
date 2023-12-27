package com.example.gitapp.ui.diagram

import com.example.gitapp.ui.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface DiagramView : BaseView {
    fun changeVisibilityProgressBar(visibility: Int)
    fun displayRepositoryItem(name: String, ownerIconUrl: String)
}