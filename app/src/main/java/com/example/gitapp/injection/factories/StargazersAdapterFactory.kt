package com.example.gitapp.injection.factories

import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.ui.stargazers.StargazersAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StargazersAdapterFactory @AssistedInject constructor(
    @Assisted("stargazersList") private val stargazersList: ArrayList<ApiStarredData>
) {
    fun createStargazersAdapter(): StargazersAdapter {
        return StargazersAdapter(stargazersList)
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("stargazersList") stargazersList: ArrayList<ApiStarredData>): StargazersAdapterFactory
    }
}