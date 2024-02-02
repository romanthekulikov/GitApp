package com.example.gitapp.injection.factories

import android.content.Context
import android.content.Intent
import com.example.gitapp.ui.diagram.DiagramActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

const val REPO_NAME_KEY = "repository_name"
const val OWNER_NAME_KAY = "owner_name"
const val STARGAZERS_COUNT_KEY = "stargazers_count"
const val OWNER_ICON_URL_KEY = "repository_owner_icon_url"

class DiagramIntentFactory @AssistedInject constructor(
    @Assisted("fromWhomContext") private val fromWhomContext: Context,
    @Assisted("repositoryName") private val repositoryName: String,
    @Assisted("ownerName") private val ownerName: String,
    @Assisted("ownerIconUrl") private val ownerIconUrl: String,
    @Assisted("stargazersCount") private val stargazersCount: Int
) {

    fun createIntent(): Intent {
        val intent = Intent(fromWhomContext, DiagramActivity::class.java)
        intent.putExtra(REPO_NAME_KEY, repositoryName)
        intent.putExtra(OWNER_NAME_KAY, ownerName)
        intent.putExtra(STARGAZERS_COUNT_KEY, stargazersCount)
        intent.putExtra(OWNER_ICON_URL_KEY, ownerIconUrl)

        return intent
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("fromWhomContext") fromWhomContext: Context,
            @Assisted("repositoryName") repositoryName: String,
            @Assisted("ownerName") ownerName: String,
            @Assisted("ownerIconUrl") ownerIconUrl: String,
            @Assisted("stargazersCount") stargazersCount: Int
        ): DiagramIntentFactory
    }
}