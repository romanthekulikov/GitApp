package com.example.gitapp.ui.main

import android.util.Log
import com.example.gitapp.data.api.GitApiService
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.ui.base.BasePresenter
import com.example.gitapp.ui.base.ERROR_GITHUB_IS_SHUTDOWN
import com.example.gitapp.ui.base.ERROR_NO_INTERNET
import com.example.gitapp.ui.base.ERROR_EXCEEDED_LIMIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor() : BasePresenter<MainView>() {

    @Inject
    lateinit var apiService: GitApiService
    fun requestGetRepo(ownerName: String, page: Int) {
        launch {
            withContext(Dispatchers.IO) {
                try {
                    val repoList = apiService.fetchOwnerRepos(ownerName = ownerName, numberOfPage = page)
                    withContext(Dispatchers.Main) {
                        showList(repoList)
                    }
                } catch (e: UnknownHostException) {
                    showLoadError(message = ERROR_NO_INTERNET, logMessage =  e.message)
                } catch (e: RuntimeException) {
                    showLoadError(message = ERROR_EXCEEDED_LIMIT, logMessage = e.message)
                } catch (e: SocketTimeoutException) {
                    showLoadError(message = ERROR_GITHUB_IS_SHUTDOWN, logMessage = e.message)
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

    private suspend fun showLoadError(message: String, logMessage: String?) {
        Log.e("api_retrofit", logMessage ?: message)
        viewState.showError(message)
        withContext(Dispatchers.Main) {
            viewState.showRecyclerError()
        }
    }
}