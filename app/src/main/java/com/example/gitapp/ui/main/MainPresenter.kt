package com.example.gitapp.ui.main

import android.util.Log
import com.example.gitapp.data.api.GitApiClient
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.ui.base.BasePresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor() : BasePresenter<MainView>() {
    fun requestGetRepo(ownerName: String, page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = GitApiClient.apiService
                val repoList = api.fetchOwnerRepos(ownerName = ownerName, numberOfPage = page).execute().body()
                CoroutineScope(Dispatchers.Main).launch {
                    showList(repoList.orEmpty())

                }
            } catch (e: Exception) {
                showRecyclerError(e.message ?: "Empty body")
            }
        }
    }

    private fun showList(repoList: List<ApiRepo>) {
        if (repoList.isNotEmpty()) {
            viewState.showRepositories(repoList)
        } else {
            viewState.showEmptyListNotification()
        }
    }

    private fun showRecyclerError(message: String) {
        Log.e("api_retrofit", message)
        CoroutineScope(Dispatchers.Main).launch {
            viewState.showRecyclerError()
        }
    }
}