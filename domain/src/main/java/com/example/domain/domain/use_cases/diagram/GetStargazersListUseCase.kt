package com.example.domain.domain.use_cases.diagram

import com.example.domain.domain.Repository
import com.example.domain.domain.entity.Stargazer
import com.example.domain.domain.models.RepoEntity
import com.example.domain.domain.models.StargazerEntity

class GetStargazersListUseCase(private val repository: Repository) {
    suspend fun execute(repo: RepoEntity, nextLoadPageNumber: Int): List<StargazerEntity> {
        return repository.getStargazersList(repo.owner.nameUser, repo.name, nextLoadPageNumber)
    }

    suspend fun execute(repo: RepoEntity): List<Stargazer> {
        return repository.getStargazersList(repo.owner.nameUser, repo.name)
    }

    fun execute(): List<Stargazer> {
        return repository.getLoadedStargazers()
    }
}