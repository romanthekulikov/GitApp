package com.example.gitapp.main.views

import com.example.gitapp.main.adapters.RepositoriesAdapter
import com.example.gitapp.retrofit.GitRepositoryResponse
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface OwnerEditTextView : MvpView {
    @StateStrategyType(value = SkipStrategy::class)
    fun showError(errorMsg: String)
    fun showRepositories(listRepos: List<GitRepositoryResponse>, adapter: RepositoriesAdapter)
    fun changeVisibilityProgressBar(visibility: Int)
}