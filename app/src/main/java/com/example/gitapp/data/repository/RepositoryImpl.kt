package com.example.gitapp.data.repository

import com.example.gitapp.data.api.GitApiService
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.data.database.AppDatabase
import com.example.gitapp.ui.diagram.DiagramMode
import com.example.gitapp.utils.PeriodHelper
import java.time.LocalDate
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {

    @Inject
    lateinit var apiService: GitApiService

    @Inject
    lateinit var periodHelper: PeriodHelper

    private var stargazersItemsList: MutableList<ApiStarredData> = mutableListOf()
    private var db = AppDatabase.db
    override suspend fun getOwnerRepoList(ownerName: String, pageNumb: Int): List<ApiRepo> {
        val repoList = apiService.fetchOwnerRepos(ownerName = ownerName, numberOfPage = pageNumb)
        for (repo in repoList) {
            repo.isFavorite = db.repoDao().isFavoriteRepo(repo.owner.nameUser, repo.name)
        }
        db.repoDao().insertAll(repoList)
        return db.repoDao().getOwnerRepos(ownerName)
    }

    override suspend fun getOwnerRepoList(ownerName: String): List<ApiRepo> {
        return db.repoDao().getOwnerRepos(ownerName)
    }

    override suspend fun getStargazersList(ownerName: String, repoName: String, page: Int): List<ApiStarredData> {
        var loadedStargazers = apiService.fetchRepoStarred(
            ownerName = ownerName,
            repository = repoName,
            page = page
        ).sortedBy { it.time }
        loadedStargazers.forEach { it.starredRepo = repoName } // Response don't have repo name

        AppDatabase.db.stargazerDao().insertAll(loadedStargazers)
        stargazersItemsList = (loadedStargazers + stargazersItemsList).toMutableList()
        loadedStargazers = db.stargazerDao().getStargazers(repoName)

        return loadedStargazers
    }

    override suspend fun getStargazersList(ownerName: String, repoName: String): List<ApiStarredData> {
        val stargazers = db.stargazerDao().getStargazers(repoName)
        stargazersItemsList = stargazers.toMutableList()
        return stargazers
    }

    override suspend fun updateFavoriteRepo(ownerName: String, repoName: String, isFavorite: Boolean) {
        db.repoDao().updateFavoriteRepo(ownerName, repoName, isFavorite)
    }

    override suspend fun isFavoriteRepo(ownerName: String, repoName: String): Boolean {
        return db.repoDao().isFavoriteRepo(ownerName, repoName)
    }

    override fun getLastDateLoadedStargazer(): LocalDate {
        return stargazersItemsList[stargazersItemsList.size - 1].time
    }

    override fun getFirstLoadedStargazerDate(): LocalDate {
        return stargazersItemsList[0].time
    }

    override fun getLoadedStargazers(): List<ApiStarredData> {
        return stargazersItemsList
    }

    override fun getLoadedDataInPeriod(
        startPeriod: LocalDate,
        endPeriod: LocalDate,
        periodType: DiagramMode
    ): List<List<ApiStarredData>> {
        return periodHelper.getDataInPeriod(startPeriod, endPeriod, periodType, stargazersItemsList)
    }

    override fun clearMemorySavedStargazers() {
        stargazersItemsList.clear()
    }
}