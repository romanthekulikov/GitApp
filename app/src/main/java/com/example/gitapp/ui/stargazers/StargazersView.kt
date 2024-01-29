package com.example.gitapp.ui.stargazers

import com.example.gitapp.ui.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface StargazersView : BaseView {
}