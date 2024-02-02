package com.example.gitapp.injection.factories

import com.example.gitapp.injection.AppComponent
import com.example.gitapp.ui.diagram.DiagramPresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DiagramPresenterFactory @AssistedInject constructor(
    @Assisted("repositoryName") private val repositoryName: String,
    @Assisted("ownerName") private val ownerName: String,
    @Assisted("ownerIconUrl") private val ownerIconUrl: String,
    @Assisted("stargazersCount") private val stargazersCount: Int,
    @Assisted("appComponent") private val appComponent: AppComponent
) {
    fun createPresenter(): DiagramPresenter {
        return DiagramPresenter(
            repositoryName = repositoryName,
            ownerName = ownerName,
            ownerIconUrl = ownerIconUrl,
            stargazersCount = stargazersCount,
            appComponent = appComponent
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("repositoryName") repositoryName: String,
            @Assisted("ownerName") ownerName: String,
            @Assisted("ownerIconUrl") ownerIconUrl: String,
            @Assisted("stargazersCount") stargazersCount: Int,
            @Assisted("appComponent") appComponent: AppComponent
        ): DiagramPresenterFactory
    }
}