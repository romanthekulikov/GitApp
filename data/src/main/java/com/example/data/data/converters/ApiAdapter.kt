package com.example.data.data.converters

import com.example.data.data.api.models.ApiRepo
import com.example.data.data.api.models.ApiStargazer
import com.example.data.data.database.AppDatabase
import com.example.domain.domain.models.RepoEntity
import com.example.domain.domain.models.StargazerEntity
import com.example.domain.domain.models.UserEntity

internal suspend fun List<ApiRepo>.toRepoEntityList(): List<RepoEntity> {
    val repoEntityList = mutableListOf<RepoEntity>()
    this.forEach { apiRepo ->
        repoEntityList.add(
            RepoEntity(
                name = apiRepo.name,
                owner = UserEntity(nameUser = apiRepo.owner.nameUser, avatarUrl = apiRepo.owner.avatarUrl),
                stargazersCount = apiRepo.stargazersCount,
                isFavorite = AppDatabase.db.repoDao().isFavoriteRepo(apiRepo.owner.nameUser, apiRepo.name)
            )
        )
    }

    return repoEntityList
}

internal fun List<ApiStargazer>.toStargazerEntityList(repoOwnerName: String, repoName: String): List<StargazerEntity> {
    val stargazerEntityList = mutableListOf<StargazerEntity>()
    this.forEach { apiStargazer ->
        stargazerEntityList.add(
            StargazerEntity(
                time = apiStargazer.time,
                user = UserEntity(nameUser = apiStargazer.user.nameUser, avatarUrl = apiStargazer.user.avatarUrl),
                repoName = repoName,
                ownerName = repoOwnerName
            )
        )
    }

    return stargazerEntityList
}