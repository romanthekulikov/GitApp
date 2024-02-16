package com.example.gitapp.injection.factories

import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.injection.AppComponent
import com.example.gitapp.ui.diagram.DiagramPresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DiagramPresenterFactory @AssistedInject constructor(
    @Assisted("repo") private val repo: ApiRepo,
    @Assisted("appComponent") private val appComponent: AppComponent
) {
    fun createPresenter(): DiagramPresenter {
        return DiagramPresenter(
            repo = repo,
            appComponent = appComponent
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("repo") repo: ApiRepo,
            @Assisted("appComponent") appComponent: AppComponent
        ): DiagramPresenterFactory
    }
}