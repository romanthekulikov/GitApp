package com.example.gitapp.injection.factories

import com.example.gitapp.entity.Stared
import com.example.gitapp.ui.stargazers.StargazersPresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StargazersPresenterFactory @AssistedInject constructor (
    @Assisted("stargazers") val stargazers: List<Stared>
) {
    fun createPresenter(): StargazersPresenter = StargazersPresenter(stargazers = ArrayList(stargazers))

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("stargazers") stargazers: List<Stared>): StargazersPresenterFactory
    }
}