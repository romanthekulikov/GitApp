package com.example.gitapp.ui.main

import com.example.gitapp.data.api.entities.GitRepositoryEntity
import com.example.gitapp.ui.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : BaseView {
    fun showRepositories(listRepos: List<GitRepositoryEntity>)
    fun changeVisibilityProgressBar(visibility: Int)
}