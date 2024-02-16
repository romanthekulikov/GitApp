package com.example.gitapp.ui.main

import android.util.Log
import com.example.gitapp.data.api.GitApiService
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.data.repository.Repository
import com.example.gitapp.ui.base.BasePresenter
import com.example.gitapp.ui.base.ERROR_EXCEEDED_LIMIT
import com.example.gitapp.ui.base.ERROR_GITHUB_IS_SHUTDOWN
import com.example.gitapp.ui.base.ERROR_NO_INTERNET
import kotlinx.coroutines.launch
import moxy.InjectViewState
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor() : BasePresenter<MainView>() {

    @Inject
    lateinit var apiService: GitApiService

    @Inject
    lateinit var repository: Repository
    fun requestGetRepo(ownerName: String, page: Int) {
        launch {
            try {
                val repoList = repository.getOwnerRepoList(ownerName = ownerName, pageNumb = page)
                showList(repoList)
            } catch (e: UnknownHostException) {
                val repoList = repository.getOwnerRepoList(ownerName = ownerName)
                showList(repoList)
                showLoadError(message = ERROR_NO_INTERNET, logMessage = e.message)
            } catch (e: RuntimeException) {
                showLoadError(message = ERROR_EXCEEDED_LIMIT, logMessage = e.message)
            } catch (e: SocketTimeoutException) {
                showLoadError(message = ERROR_GITHUB_IS_SHUTDOWN, logMessage = e.message)
            }
        }
    }

    fun requestChangeFavoriteRepo(repo: ApiRepo, isFavorite: Boolean) {
        launch {
            repository.updateFavoriteRepo(repo.owner.nameUser, repo.name, isFavorite)
        }
    }

    private fun showList(repoList: List<ApiRepo>) {
        if (repoList.isNotEmpty()) {
            viewState.showRepositories(repoList)
        } else {
            viewState.showEmptyNotificationList()
        }
    }

    private fun showLoadError(message: String, logMessage: String?) {
        Log.e("api_retrofit", logMessage ?: message)
        viewState.showError(message)
        viewState.showRecyclerError()
    }
}