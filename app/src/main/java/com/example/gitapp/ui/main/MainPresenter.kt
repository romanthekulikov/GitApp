package com.example.gitapp.ui.main

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.gitapp.data.api.entities.GitRepositoryEntity
import com.example.gitapp.ui.main.paging.RepoPositionalDataSourceFactory
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.concurrent.Executors


const val INTERNET_CONNECTION_ERROR = "No internet connection"

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    fun requestGetRepositories(connectivityManager: ConnectivityManager, ownerName: String) {
        if (hasConnection(connectivityManager)) {
            viewState.changeVisibilityProgressBar(View.VISIBLE)
            val dataSourceFactory = RepoPositionalDataSourceFactory(ownerName)
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(100)
                .build()

            val pagedListLiveData: LiveData<PagedList<GitRepositoryEntity>> = LivePagedListBuilder(dataSourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setBoundaryCallback(object : PagedList.BoundaryCallback<GitRepositoryEntity>() {
                    override fun onZeroItemsLoaded() {
                        viewState.showEmptyListRepo()
                        viewState.changeVisibilityProgressBar(View.GONE)
                    }

                    override fun onItemAtFrontLoaded(itemAtFront: GitRepositoryEntity) {
                        viewState.changeVisibilityProgressBar(View.GONE)
                    }
                })
                .build()

            viewState.showRepositories(pagedListLiveData)
        } else {
            viewState.showError(INTERNET_CONNECTION_ERROR)
        }
    }

    private fun hasConnection(connectivityManager: ConnectivityManager): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
}