package com.example.gitapp.ui.main

import android.util.Log
import com.example.gitapp.data.api.GitApiService
import com.example.gitapp.data.database.entity.RepoEntity
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

    private var needNewPage = true

    private var showError = false

    fun requestGetRepo(ownerName: String, page: Int, requireNewPage: Boolean = needNewPage, showErrorOnFailLoad: Boolean) {
        launch {
            try {
                showError = showErrorOnFailLoad
                if (requireNewPage) {
                    val repoList = repository.getOwnerRepoList(ownerName = ownerName, pageNumb = page)
                    showList(repoList)
                }
            } catch (e: UnknownHostException) {
                showLoadError(message = ERROR_NO_INTERNET, logMessage = e.message, ownerName = ownerName)
            } catch (e: RuntimeException) {
                showLoadError(message = ERROR_EXCEEDED_LIMIT, logMessage = e.message, ownerName = ownerName)
            } catch (e: SocketTimeoutException) {
                showLoadError(message = ERROR_GITHUB_IS_SHUTDOWN, logMessage = e.message, ownerName = ownerName)
            }
        }
    }

    fun requestChangeFavoriteRepo(repo: RepoEntity, isFavorite: Boolean) {
        launch {
            repository.updateFavoriteRepo(repo.owner.nameUser, repo.name, isFavorite)
        }
    }

    private fun showList(repoList: List<RepoEntity>) {
        if (repoList.isNotEmpty()) {
            viewState.showLoadedRepositories(repoList)
        } else {
            viewState.showEmptyNotificationList()
        }
    }

    private suspend fun showLoadError(message: String, logMessage: String?, ownerName: String) {
        needNewPage = false
        showList(repository.getOwnerRepoList(ownerName = ownerName))
        if (showError) {
            Log.e("api_retrofit", logMessage ?: message)
            viewState.showError(message)
            viewState.showRecyclerError()
        }
    }
}