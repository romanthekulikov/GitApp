package com.example.gitapp.data.repository

import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.data.database.entity.StargazerEntity
import com.example.gitapp.entity.Stared
import com.example.gitapp.entity.Stargazer
import com.example.gitapp.ui.diagram.PeriodType
import java.time.LocalDate

interface Repository {
    suspend fun getOwnerRepoList(ownerName: String, pageNumb: Int): List<RepoEntity>
    suspend fun getOwnerRepoList(ownerName: String): List<RepoEntity>
    suspend fun getStargazersList(ownerName: String, repoName: String, page: Int): List<StargazerEntity>
    suspend fun getStargazersList(ownerName: String, repoName: String): List<Stargazer>
    suspend fun updateFavoriteRepo(ownerName: String, repoName: String, isFavorite: Boolean)
    suspend fun isFavoriteRepo(ownerName: String, repoName: String): Boolean
    fun getLastDateLoadedStargazer(): LocalDate
    fun getFirstLoadedStargazerDate(): LocalDate
    fun getLoadedStargazers(): List<Stargazer>
    fun getLoadedDataInPeriod(startPeriod: LocalDate, endPeriod: LocalDate, periodType: PeriodType): List<Stared>
    fun clearMemorySavedStargazers()
}