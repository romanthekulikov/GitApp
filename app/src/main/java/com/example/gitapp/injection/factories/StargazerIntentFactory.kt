package com.example.gitapp.injection.factories

import android.content.Context
import android.content.Intent
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.ui.stargazers.StargazersActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

const val PERIOD_KEY = "period"
const val STARGAZERS_KEY = "stargazers"

class StargazerIntentFactory @AssistedInject constructor(
    @Assisted("fromWhomContext") private val fromWhomContext: Context,
    @Assisted("period") private val period: String,
    @Assisted("stargazers") private val stargazers: ArrayList<ApiStarredData>
) {
    fun createIntent(): Intent {
        val intent = Intent(fromWhomContext, StargazersActivity::class.java)
        intent.putExtra(PERIOD_KEY, period)
        intent.putParcelableArrayListExtra(STARGAZERS_KEY, stargazers)

        return intent
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("fromWhomContext") fromWhomContext: Context,
            @Assisted("period") period: String,
            @Assisted("stargazers") stargazers: ArrayList<ApiStarredData>
        ): StargazerIntentFactory
    }
}