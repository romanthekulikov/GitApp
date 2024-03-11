package com.example.gitapp.data.repository

import com.example.gitapp.data.api.GitApiService
import com.example.gitapp.data.converters.toRepoEntityList
import com.example.gitapp.data.converters.toStargazerEntityList
import com.example.gitapp.data.database.AppDatabase
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.data.database.entity.StargazerEntity
import com.example.gitapp.entity.Repo
import com.example.gitapp.entity.Stared
import com.example.gitapp.entity.Stargazer
import com.example.gitapp.ui.diagram.PeriodType
import com.example.gitapp.ui.diagram.utils.PeriodHelper
import java.time.LocalDate
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: GitApiService,
    private val periodHelper: PeriodHelper
) : Repository {

    private var stargazersItemList: MutableList<Stargazer> = mutableListOf()
    private var db = AppDatabase.db
    override suspend fun getOwnerRepoList(ownerName: String, pageNumb: Int): List<RepoEntity> {
        val repoList = apiService.fetchOwnerRepos(ownerName = ownerName, numberOfPage = pageNumb)
        val repoEntityList = repoList.toRepoEntityList().toMutableList()
        db.repoDao().insertAll(repoEntityList)

        return getOwnerRepoList(ownerName)
    }

    override suspend fun getOwnerRepoList(ownerName: String): List<RepoEntity> {
        return db.repoDao().getOwnerRepoList(ownerName).toMutableList()
    }

    override suspend fun getStargazersList(ownerName: String, repoName: String, page: Int): List<StargazerEntity> {
        val loadedStargazers = apiService.fetchRepoStarred(
            ownerName = ownerName,
            repository = repoName,
            page = page
        ).sortedBy { it.time }
        val stargazerEntityList = loadedStargazers.toStargazerEntityList(ownerName, repoName)
        db.stargazerDao().insertAll(stargazerEntityList)
        stargazersItemList = (loadedStargazers + stargazersItemList).distinct().sortedBy { it.time }.toMutableList()

        return stargazerEntityList
    }

    override suspend fun getStargazersList(ownerName: String, repoName: String): List<Stargazer> {
        val stargazers = db.stargazerDao().getStargazers(repoName, ownerName)
        stargazersItemList = stargazers.sortedBy { it.time }.toMutableList()
        return stargazers
    }

    override suspend fun updateRepoFavorite(ownerName: String, repoName: String, isFavorite: Boolean) {
        db.repoDao().updateRepoFavorite(ownerName, repoName, isFavorite)
    }

    override suspend fun isFavoriteRepo(ownerName: String, repoName: String): Boolean {
        return db.repoDao().isFavoriteRepo(ownerName, repoName)
    }

    override suspend fun getFavoriteRepoList(): List<Repo> {
        return db.repoDao().getFavoriteRepoList()
    }

    override suspend fun getRepoFromApi(ownerName: String, repoName: String): Repo {
        return apiService.fetchOwnerRepo(ownerName = ownerName, repo = repoName)
    }

    override suspend fun updateRepoStargazersCount(ownerName: String, repoName: String, stargazersCount: Int) {
        db.repoDao().updateRepoStargazersCount(stargazersCount = stargazersCount, ownerName = ownerName, repoName = repoName)
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