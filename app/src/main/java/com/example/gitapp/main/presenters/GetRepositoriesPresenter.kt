package com.example.gitapp.main.presenters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.gitapp.main.adapters.RepositoriesAdapter
import com.example.gitapp.main.views.OwnerEditTextView
import com.example.gitapp.retrofit.GitApiClient
import com.example.gitapp.retrofit.GitRepositoryResponse
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val FETCH_REPOSITORY_ERROR = "List is empty"

@InjectViewState
class GetRepositoriesPresenter : MvpPresenter<OwnerEditTextView>() {
    private var repositoryList: List<GitRepositoryResponse>? = mutableListOf()
    private var ownerIcon: Bitmap? = null

    fun getRepositories(ownerName: String, listener: RepositoriesAdapter.RepositoryClickListener, context: Context) {
        viewState.changeVisibilityProgressBar(View.VISIBLE)
        val client = GitApiClient
            .apiService
            .fetchOwnerRepositories(ownerName = ownerName)

        client.enqueue(object : Callback<List<GitRepositoryResponse>> {
            override fun onResponse(
                call: Call<List<GitRepositoryResponse>>,
                response: Response<List<GitRepositoryResponse>>
            ) {
                repositoryList = response.body()

                try {
                    showRepositories(listener = listener, context = context)
                } catch (ex: NullPointerException) {
                    viewState.showError(FETCH_REPOSITORY_ERROR)
                } catch (ex: IndexOutOfBoundsException) {
                    viewState.showError(FETCH_REPOSITORY_ERROR)
                } finally {
                    viewState.changeVisibilityProgressBar(View.GONE)
                }
            }

            override fun onFailure(call: Call<List<GitRepositoryResponse>>, t: Throwable) {
                try {
                    viewState.showError(t.message!!)
                } catch (_: NullPointerException) {
                }
            }

        })
    }

    fun showRepositories(listener: RepositoriesAdapter.RepositoryClickListener, context: Context) {
        ownerIcon = Bitmap.createBitmap(30, 30, Bitmap.Config.ARGB_8888)
        val ownerIconUrl = repositoryList!![0].owner.avatarUrl
        Glide.with(context)
            .asBitmap()
            .load(ownerIconUrl)
            .circleCrop()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    ownerIcon = resource
                    val repositoriesAdapter = RepositoriesAdapter(
                        repositoryList = repositoryList!!,
                        ownerImage = ownerIcon!!,
                        listener = listener
                    )
                    viewState.showRepositories(listRepos = repositoryList!!, adapter = repositoriesAdapter)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    val repositoriesAdapter = RepositoriesAdapter(
                        repositoryList = repositoryList!!,
                        ownerImage = ownerIcon!!,
                        listener = listener
                    )
                    viewState.showRepositories(listRepos = repositoryList!!, adapter = repositoriesAdapter)
                }
            })
    }
}