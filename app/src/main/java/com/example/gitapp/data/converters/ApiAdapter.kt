package com.example.gitapp.data.converters

import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.data.api.models.ApiStargazer
import com.example.gitapp.data.database.AppDatabase
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.data.database.entity.StargazerEntity
import com.example.gitapp.data.database.entity.UserEntity

suspend fun List<ApiRepo>.toRepoEntityList(): List<RepoEntity> {
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

fun List<ApiStargazer>.toStargazerEntityList(repoOwnerName: String, repoName: String): List<StargazerEntity> {
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