package com.example.gitapp.injection.factories

import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.ui.stargazers.StargazersPresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StargazersPresenterFactory @AssistedInject constructor (
    @Assisted("stargazers") val stargazers: ArrayList<ApiStarredData>
) {
    fun createPresenter(): StargazersPresenter = StargazersPresenter(stargazers = stargazers)

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("stargazers") stargazers: java.util.ArrayList<ApiStarredData>): StargazersPresenterFactory
    }
}