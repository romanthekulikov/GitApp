package com.example.gitapp.ui.stargazers

import com.example.gitapp.data.api.models.ApiStargazer
import com.example.gitapp.data.api.models.ApiUser
import com.example.gitapp.entity.Stargazer
import com.example.gitapp.entity.Stared
import com.example.gitapp.ui.base.BasePresenter
import moxy.InjectViewState

@InjectViewState
class StargazersPresenter(val stargazers: List<Stared>) : BasePresenter<StargazersView>() {
    init {
        viewState.initRecyclerView(starredToStargazerList(stargazers))
    }

    private fun starredToStargazerList(staredData: List<Stared>): List<Stargazer> {
        val stargazerList = mutableListOf<Stargazer>()
        for (starred in staredData) {
            starred.users.forEach { user ->
                stargazerList.add(
                    ApiStargazer(
                        time = starred.time,
                        user = ApiUser(user)
                    )
                )
            }
        }

        return stargazerList
    }
}