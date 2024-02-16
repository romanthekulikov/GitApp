package com.example.gitapp.injection.factories

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.ui.diagram.DiagramActivity
import com.example.gitapp.ui.diagram.REPO_KEY
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DiagramIntentFactory @AssistedInject constructor(
    @Assisted("fromWhomContext") private val fromWhomContext: Context,
    @Assisted("repo") private val repo: ApiRepo
) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun createIntent(): Intent {
        val intent = Intent(fromWhomContext, DiagramActivity::class.java)
        intent.putExtra(REPO_KEY, repo)

        return intent
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("fromWhomContext") fromWhomContext: Context,
            @Assisted("repo") repo: ApiRepo
        ): DiagramIntentFactory
    }
}