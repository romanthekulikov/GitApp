package com.example.gitapp.ui.stargazers

import com.example.domain.domain.entity.Stared
import com.example.domain.domain.use_cases.stargazers.GetStargazerListUseCase
import com.example.gitapp.ui.base.BasePresenter
import moxy.InjectViewState

@InjectViewState
class StargazersPresenter(stargazers: List<Stared>) : BasePresenter<StargazersView>() {
    private val getStargazerListUseCase = GetStargazerListUseCase()

    init {
        viewState.initRecyclerView(
            stargazers = getStargazerListUseCase.execute(stargazers)
        )
    }
}