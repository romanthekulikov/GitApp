package com.example.domain.domain

import com.example.domain.domain.entity.Repo
import com.example.domain.domain.entity.Stared
import com.example.domain.domain.entity.Stargazer
import com.example.domain.domain.models.RepoEntity
import com.example.domain.domain.models.StargazerEntity
import java.time.LocalDate
import kotlin.time.Duration

interface Repository {
    suspend fun getOwnerRepoList(ownerName: String, pageNumb: Int): List<RepoEntity>
    suspend fun getOwnerRepoList(ownerName: String): List<RepoEntity>
    suspend fun getStargazersList(ownerName: String, repoName: String, page: Int): List<StargazerEntity>
    suspend fun getStargazersList(ownerName: String, repoName: String): List<Stargazer>
    suspend fun updateRepoFavorite(ownerName: String, repoName: String, isFavorite: Boolean)
    suspend fun isFavoriteRepo(ownerName: String, repoName: String): Boolean
    suspend fun getFavoriteRepoList(): List<Repo>
    suspend fun getRepoFromApi(ownerName: String, repoName: String): Repo?
    fun getUntilLimitResetTimeSec(): Duration
    suspend fun updateRepoStargazersCount(ownerName: String, repoName: String, stargazersCount: Int)
    fun getLastLoadedStargazerDate(): LocalDate
    fun getFirstLoadedStargazerDate(): LocalDate
    fun getLoadedStargazers(): List<Stargazer>
    fun getLoadedDataInPeriod(startPeriod: LocalDate, endPeriod: LocalDate): List<Stared>
    fun clearMemorySavedStargazers()
    suspend fun getRepoEntity(ownerName: String, repoName: String): RepoEntity?
}