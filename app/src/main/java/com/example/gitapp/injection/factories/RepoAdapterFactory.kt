package com.example.gitapp.injection.factories

import com.example.gitapp.ui.main.RepoAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class RepoAdapterFactory @AssistedInject constructor(
    @Assisted("helper") private val helper: RepoAdapter.RepoRecyclerHelper
) {
    fun createRepoAdapter(): RepoAdapter {
        return RepoAdapter(helper)
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("helper") helper: RepoAdapter.RepoRecyclerHelper): RepoAdapterFactory
    }
}