package com.example.gitapp.ui.main

import com.example.gitapp.data.api.entities.ApiRepo
import com.example.gitapp.ui.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : BaseView {
    fun showRepositories(listRepo: List<ApiRepo>)
    fun showRecyclerError()
    fun showEmptyListNotification()
}