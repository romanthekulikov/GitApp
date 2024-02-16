package com.example.gitapp.data.repository

import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.ui.diagram.DiagramMode
import java.time.LocalDate

interface Repository {
    suspend fun getOwnerRepoList(ownerName: String, pageNumb: Int): List<ApiRepo>
    suspend fun getOwnerRepoList(ownerName: String): List<ApiRepo>
    suspend fun getStargazersList(ownerName: String, repoName: String, page: Int): List<ApiStarredData>
    suspend fun getStargazersList(ownerName: String, repoName: String): List<ApiStarredData>
    suspend fun updateFavoriteRepo(ownerName: String, repoName: String, isFavorite: Boolean)
    suspend fun isFavoriteRepo(ownerName: String, repoName: String): Boolean
    fun getLastDateLoadedStargazer(): LocalDate
    fun getFirstLoadedStargazerDate(): LocalDate
    fun getLoadedStargazers(): List<ApiStarredData>
    fun getLoadedDataInPeriod(startPeriod: LocalDate, endPeriod: LocalDate, periodType: DiagramMode): List<List<ApiStarredData>>
    fun clearMemorySavedStargazers()
}