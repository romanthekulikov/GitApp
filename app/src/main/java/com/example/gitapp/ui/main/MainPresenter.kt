package com.example.gitapp.ui.main

import android.util.Log
import com.example.gitapp.data.api.GitApiClient
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.ui.base.BasePresenter
import com.example.gitapp.ui.base.ERROR_GITHUB_IS_SHUTDOWN
import com.example.gitapp.ui.base.ERROR_NO_INTERNET
import com.example.gitapp.ui.base.ERROR_TIMED_OUT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {
    fun requestGetRepo(ownerName: String, page: Int) {
        launch {
            withContext(Dispatchers.IO) {
                try {
                    val api = GitApiClient.apiService
                    val repoList = api.fetchOwnerRepos(ownerName = ownerName, numberOfPage = page)
                    withContext(Dispatchers.Main) {
                        showList(repoList)
                    }
                } catch (e: UnknownHostException) {
                    showLoadError(e.message ?: ERROR_NO_INTERNET)
                } catch (e: RuntimeException) {
                    showLoadError(e.message ?: ERROR_TIMED_OUT)
                } catch (e: SocketTimeoutException) {
                    showLoadError(e.message ?: ERROR_GITHUB_IS_SHUTDOWN)
                }
            }
        }
    }

    private fun showList(repoList: List<ApiRepo>) {
        if (repoList.isNotEmpty()) {
            viewState.showRepositories(repoList)
        } else {
            viewState.showEmptyNotificationList()
        }
    }

    private suspend fun showLoadError(message: String) {
        Log.e("api_retrofit", message)
        withContext(Dispatchers.Main) {
            viewState.showRecyclerError()
        }
    }
}