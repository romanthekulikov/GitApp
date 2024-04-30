package com.example.domain.domain.use_cases.main

import com.example.domain.domain.Repository
import com.example.domain.domain.models.RepoEntity

class GetRepoListUseCase(private val repository: Repository) {
    suspend fun execute(ownerName: String, page: Int): List<RepoEntity> {
        return repository.getOwnerRepoList(ownerName = ownerName, pageNumb = page)
    }

    suspend fun execute(ownerName: String): List<RepoEntity> {
        return repository.getOwnerRepoList(ownerName = ownerName)
    }
}