package com.example.domain.domain.use_cases.main

import com.example.domain.domain.Repository
import com.example.domain.domain.entity.Repo

class UpdateRepoFavoriteUseCase(private val repository: Repository) {
    suspend fun execute(repo: Repo, isFavorite: Boolean) {
        repository.updateRepoFavorite(repo.owner.nameUser, repo.name, isFavorite)
    }
}