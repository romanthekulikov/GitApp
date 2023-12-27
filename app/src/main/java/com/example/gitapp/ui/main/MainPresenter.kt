package com.example.gitapp.ui.main

import android.view.View
import com.example.gitapp.data.api.GitApiClient
import com.example.gitapp.data.api.entities.GitRepositoryEntity
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val FETCH_REPOSITORY_ERROR = "List is empty"

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    private var repositoryList: List<GitRepositoryEntity>? = mutableListOf()

    fun requestGetRepositories(ownerName: String) {
        viewState.changeVisibilityProgressBar(View.VISIBLE)
        val client = GitApiClient.apiService
            .fetchOwnerRepositories(ownerName = ownerName, 100)

        client.enqueue(object : Callback<List<GitRepositoryEntity>> {
            override fun onResponse(
                call: Call<List<GitRepositoryEntity>>,
                response: Response<List<GitRepositoryEntity>>
            ) {
                repositoryList = response.body()
                try {
                    viewState.showRepositories(listRepos = repositoryList!!)
                } catch (ex: NullPointerException) {
                    viewState.showError(FETCH_REPOSITORY_ERROR)
                } catch (ex: IndexOutOfBoundsException) {
                    viewState.showError(FETCH_REPOSITORY_ERROR)
                } finally {
                    viewState.changeVisibilityProgressBar(View.GONE)
                }
            }

            override fun onFailure(call: Call<List<GitRepositoryEntity>>, t: Throwable) {
                try {
                    viewState.showError(t.message!!)
                } catch (_: NullPointerException) {}
            }

        })
    }
}