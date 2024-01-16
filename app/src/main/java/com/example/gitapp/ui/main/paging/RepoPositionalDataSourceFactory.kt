package com.example.gitapp.ui.main.paging

import androidx.paging.DataSource
import com.example.gitapp.data.api.entities.GitRepositoryEntity

class RepoPositionalDataSourceFactory(private val ownerName: String) : DataSource.Factory<Int, GitRepositoryEntity>() {
    override fun create(): DataSource<Int, GitRepositoryEntity> {
        return RepoPositionalDataSource(ownerName)
    }
}