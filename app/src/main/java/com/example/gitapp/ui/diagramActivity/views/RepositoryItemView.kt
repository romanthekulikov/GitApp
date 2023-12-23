package com.example.gitapp.ui.diagramActivity.views

import android.graphics.Bitmap
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface RepositoryItemView : MvpView {
    fun displayRepositoryItem(name: String, ownerIcon: Bitmap)
}