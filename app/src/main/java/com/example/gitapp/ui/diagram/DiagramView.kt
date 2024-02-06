package com.example.gitapp.ui.diagram

import com.example.gitapp.ui.base.BaseView
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.ValueFormatter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface DiagramView : BaseView {
    fun changeVisibilityProgressBar(visibility: Int)
    fun displayRepositoryItem(name: String, ownerIconUrl: String)
    fun displayData(data: BarData, valueFormatter: ValueFormatter)
    fun setPreviousButtonEnabled(isEnabled: Boolean)
    fun setNextButtonEnabled(isEnabled: Boolean)
}