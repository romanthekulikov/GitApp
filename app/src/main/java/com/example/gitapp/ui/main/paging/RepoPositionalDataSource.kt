package com.example.gitapp.ui.main.paging

import androidx.paging.PositionalDataSource
import com.example.gitapp.data.api.GitApiClient
import com.example.gitapp.data.api.entities.GitRepositoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepoPositionalDataSource(private val ownerName: String) : PositionalDataSource<GitRepositoryEntity>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<GitRepositoryEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            val api = GitApiClient.apiService
            val result = api
                .fetchOwnerRepositories(ownerName, params.requestedStartPosition, params.pageSize)
                .execute()
                .body()
            CoroutineScope(Dispatchers.Main).launch {
                if (result != null) {
                    callback.onResult(result, 0)
                } else {
                    callback.onResult(listOf(), 0)
                }
            }

        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<GitRepositoryEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            val api = GitApiClient.apiService
            val result = api
                .fetchOwnerRepositories(ownerName, params.startPosition / params.loadSize, params.loadSize)
                .execute()
                .body()
            CoroutineScope(Dispatchers.Main).launch {
                if (result != null) {
                    callback.onResult(result)
                } else {
                    callback.onResult(listOf())
                }
            }
        }
    }
}