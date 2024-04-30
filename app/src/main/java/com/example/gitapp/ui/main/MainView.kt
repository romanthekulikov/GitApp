package com.example.gitapp.ui.main

import com.example.domain.domain.models.RepoEntity
import com.example.gitapp.ui.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : BaseView {
    fun showLoadedRepositories(listRepo: List<RepoEntity>)
    fun showRecyclerError()
    fun showEmptyNotificationList()
}