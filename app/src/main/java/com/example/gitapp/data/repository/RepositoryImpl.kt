package com.example.gitapp.data.repository

import com.example.gitapp.data.api.GitApiService
import com.example.gitapp.data.converters.toRepoEntityList
import com.example.gitapp.data.converters.toStargazerEntityList
import com.example.gitapp.data.database.AppDatabase
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.data.database.entity.StargazerEntity
import com.example.gitapp.entity.Stared
import com.example.gitapp.entity.Stargazer
import com.example.gitapp.ui.diagram.PeriodType
import com.example.gitapp.utils.PeriodHelper
import java.time.LocalDate
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {

    @Inject
    lateinit var apiService: GitApiService

    @Inject
    lateinit var periodHelper: PeriodHelper

    private var stargazersItemList: MutableList<Stargazer> = mutableListOf()
    private var db = AppDatabase.db
    override suspend fun getOwnerRepoList(ownerName: String, pageNumb: Int): List<RepoEntity> {
        val repoList = apiService.fetchOwnerRepos(ownerName = ownerName, numberOfPage = pageNumb)
        val repoEntityList = repoList.toRepoEntityList().toMutableList()
        db.repoDao().insertAll(repoEntityList)

        return getOwnerRepoList(ownerName)
    }

    override suspend fun getOwnerRepoList(ownerName: String): List<RepoEntity> {
        return db.repoDao().getOwnerRepos(ownerName).toMutableList()
    }

    override suspend fun getStargazersList(ownerName: String, repoName: String, page: Int): List<StargazerEntity> {
        val loadedStargazers = apiService.fetchRepoStarred(
            ownerName = ownerName,
            repository = repoName,
            page = page
        ).sortedBy { it.time }
        val stargazerEntityList = loadedStargazers.toStargazerEntityList(ownerName, repoName)
        db.stargazerDao().insertAll(stargazerEntityList)
        stargazersItemList = (loadedStargazers + stargazersItemList).toMutableList()

        return stargazerEntityList
    }

    override suspend fun getStargazersList(ownerName: String, repoName: String): List<Stargazer> {
        val stargazers = db.stargazerDao().getStargazers(repoName, ownerName)
        stargazersItemList = stargazers.toMutableList()
        return stargazers
    }

    override suspend fun updateFavoriteRepo(ownerName: String, repoName: String, isFavorite: Boolean) {
        db.repoDao().updateFavoriteRepo(ownerName, repoName, isFavorite)
    }

    override suspend fun isFavoriteRepo(ownerName: String, repoName: String): Boolean {
        return db.repoDao().isFavoriteRepo(ownerName, repoName)
    }

    override fun getLastDateLoadedStargazer(): LocalDate {
        return stargazersItemList[stargazersItemList.size - 1].time
    }

    override fun getFirstLoadedStargazerDate(): LocalDate {
        return stargazersItemList[0].time
    }

    override fun getLoadedStargazers(): List<Stargazer> {
        return stargazersItemList
    }

    override fun getLoadedDataInPeriod(
        startPeriod: LocalDate,
        endPeriod: LocalDate,
        periodType: PeriodType
    ): List<Stared> {
        val starredList = periodHelper.getStarred(stargazersItemList)
        return periodHelper.getDataInPeriod(startPeriod, endPeriod, starredList)
    }

    override fun clearMemorySavedStargazers() {
        stargazersItemList.clear()
    }
}