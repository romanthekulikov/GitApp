package com.example.gitapp.ui.stargazers

import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.ui.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.util.ArrayList

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface StargazersView : BaseView {
    fun initRecyclerView(stargazers: ArrayList<ApiStarredData>)
}