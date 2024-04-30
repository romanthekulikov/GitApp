package com.example.domain.domain.use_cases.diagram

import com.example.domain.domain.Repository
import com.example.domain.domain.models.RepoEntity

class UpdateRepoStargazersCountUseCase(private val repository: Repository) {
    suspend fun execute(repo: RepoEntity) {
        repository.updateRepoStargazersCount(repo.owner.nameUser, repo.name, repo.stargazersCount)
    }
}