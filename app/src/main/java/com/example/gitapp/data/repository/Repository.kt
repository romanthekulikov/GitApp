package com.example.gitapp.data.repository

import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.data.database.entity.StargazerEntity
import com.example.gitapp.entity.Repo
import com.example.gitapp.entity.Stared
import com.example.gitapp.entity.Stargazer
import com.example.gitapp.ui.diagram.PeriodType
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
    suspend fun getRepoFromApi(ownerName: String, repoName: String): ApiRepo?
    fun getUntilLimitResetTimeSec(): Duration
    suspend fun updateRepoStargazersCount(ownerName: String, repoName: String, stargazersCount: Int)
    suspend fun getGithubResetLimitTime(): Long
    fun getLastDateLoadedStargazer(): LocalDate
    fun getFirstLoadedStargazerDate(): LocalDate
    fun getLoadedStargazers(): List<Stargazer>
    fun getLoadedDataInPeriod(startPeriod: LocalDate, endPeriod: LocalDate, periodType: PeriodType): List<Stared>
    fun clearMemorySavedStargazers()
    suspend fun getRepoEntity(ownerName: String, repoName: String): RepoEntity?
}