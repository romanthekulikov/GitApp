package com.example.gitapp.ui.stargazers

import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.ui.base.BasePresenter
import moxy.InjectViewState

@InjectViewState
class StargazersPresenter(val stargazers: ArrayList<ApiStarredData>) : BasePresenter<StargazersView>() {
    init {
        viewState.initRecyclerView(stargazers)
    }
}