package com.example.gitapp.ui.mainActivity.views

import com.example.gitapp.ui.mainActivity.adapters.RepositoriesAdapter
import com.example.gitapp.retrofit.entities.GitRepositoryEntity
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface OwnerEditTextView : MvpView {
    @StateStrategyType(value = SkipStrategy::class)
    fun showError(errorMsg: String)
    fun showRepositories(listRepos: List<GitRepositoryEntity>, adapter: RepositoriesAdapter)
    fun changeVisibilityProgressBar(visibility: Int)
}