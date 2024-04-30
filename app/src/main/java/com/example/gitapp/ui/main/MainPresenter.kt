package com.example.gitapp.ui.main

import android.util.Log
import com.example.domain.domain.Repository
import com.example.domain.domain.use_cases.main.GetRepoListUseCase
import com.example.domain.domain.use_cases.main.UpdateRepoFavoriteUseCase
import com.example.gitapp.App
import com.example.gitapp.ui.base.BasePresenter
import com.example.gitapp.ui.base.ERROR_EXCEEDED_LIMIT
import com.example.gitapp.ui.base.ERROR_GITHUB_IS_SHUTDOWN
import com.example.gitapp.ui.base.ERROR_NO_INTERNET
import com.example.gitapp.ui.base.ERROR_UNIDENTIFIED
import kotlinx.coroutines.launch
import moxy.InjectViewState
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    @Inject
    lateinit var repository: Repository

    private val getRepoListUseCase by lazy { GetRepoListUseCase(repository) }
    private val updateRepoFavoriteUseCase by lazy { UpdateRepoFavoriteUseCase(repository) }

    private var needNewPage = true

    private var needShowError = false

    init {
        App.appComponent.inject(this)
    }

    fun requestGetRepo(ownerName: String, page: Int, requireNewPage: Boolean = needNewPage, showErrorOnFailLoad: Boolean) {
        launch {
            try {
                needShowError = showErrorOnFailLoad
                if (requireNewPage) {
                    val repoList = getRepoListUseCase.execute(ownerName, page)
                    showList(repoList)
                }
            } catch (e: UnknownHostException) {
                showLoadError(message = ERROR_NO_INTERNET, logMessage = e.message, ownerName = ownerName)
            } catch (e: HttpException) {
                showLoadError(message = ERROR_EXCEEDED_LIMIT, logMessage = e.message, ownerName = ownerName)
            } catch (e: SocketTimeoutException) {
                showLoadError(message = ERROR_GITHUB_IS_SHUTDOWN, logMessage = e.message, ownerName = ownerName)
            } catch (e: IOException) {
                showLoadError(message = ERROR_UNIDENTIFIED, logMessage = e.message, ownerName = ownerName)
            }
        }
    }

    fun requestChangeFavoriteRepo(repo: com.example.domain.domain.models.RepoEntity, isFavorite: Boolean) {
        launch {
            updateRepoFavoriteUseCase.execute(repo, isFavorite)
        }
    }

    private fun showList(repoList: List<com.example.domain.domain.models.RepoEntity>) {
        if (repoList.isNotEmpty()) {
            viewState.showLoadedRepositories(repoList)
        } else {
            viewState.showEmptyNotificationList()
        }
    }

    private suspend fun showLoadError(message: String, logMessage: String?, ownerName: String) {
        needNewPage = false
        showList(getRepoListUseCase.execute(ownerName))
        if (needShowError) {
            Log.e("api_retrofit", logMessage ?: message)
            viewState.showError(message)
            viewState.showRecyclerError()
        }
    }
}