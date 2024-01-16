package com.example.gitapp.ui.main

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.gitapp.data.api.entities.GitRepositoryEntity
import com.example.gitapp.ui.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : BaseView {
    fun showRepositories(pagedListLiveData: LiveData<PagedList<GitRepositoryEntity>>)

    fun showEmptyListRepo()
    fun changeVisibilityProgressBar(visibility: Int)
}