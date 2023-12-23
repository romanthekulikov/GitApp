package com.example.gitapp.ui.diagramActivity.presenters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.gitapp.ui.IntentKeys
import com.example.gitapp.ui.diagramActivity.views.RepositoryItemView
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class RepositoryItemPresenter : MvpPresenter<RepositoryItemView>() {
    var repositoryName = ""
    var ownerIcon = Bitmap.createBitmap(30, 30, Bitmap.Config.ARGB_8888)
    fun displayRepository(extras: Bundle, context: Context) {
        repositoryName = extras.getString(IntentKeys.repositoryName, "")
        val ownerIconUrl = extras.getString(IntentKeys.repositoryOwnerIconUrl, "")
        Glide.with(context)
            .asBitmap()
            .circleCrop()
            .load(ownerIconUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    ownerIcon = resource
                    viewState.displayRepositoryItem(name = repositoryName, ownerIcon = ownerIcon)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    viewState.displayRepositoryItem(name = repositoryName, ownerIcon = ownerIcon)
                }
            })
    }
}