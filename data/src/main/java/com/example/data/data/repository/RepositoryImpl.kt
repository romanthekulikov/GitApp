package com.example.data.data.repository

import com.example.data.data.api.GitApiService
import com.example.data.data.api.models.ApiRepo
import com.example.data.data.converters.toRepoEntityList
import com.example.data.data.converters.toStargazerEntityList
import com.example.data.data.database.AppDatabase
import com.example.data.data.database.entity.RepoEntity
import com.example.data.data.database.entity.StargazerEntity
import com.example.domain.domain.entity.Repo
import com.example.domain.domain.entity.Stared
import com.example.domain.domain.entity.Stargazer
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

const val BASE_RESET_LIMIT_PERIOD_MIN: Long = 70

class RepositoryImpl @Inject constructor(
    private val apiService: GitApiService,
    private val periodHelper: com.example.domain.domain.PeriodHelper
) : Repository {

    private var stargazersItemList: MutableList<Stargazer> = mutableListOf()
    private var untilLimitResetTime = LocalDateTime.now().plusMinutes(BASE_RESET_LIMIT_PERIOD_MIN).toEpochSecond(ZoneOffset.UTC)
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

    override suspend fun getRepoFromApi(ownerName: String, repoName: String): Repo? {
        val repoResponse = apiService.fetchOwnerRepo(ownerName = ownerName, repo = repoName)
        if (repoResponse.isSuccessful) {
            return repoResponse.body()
        } else {
            setUntilResetLimitTime(repoResponse)
            throw retrofit2.HttpException(repoResponse)
        }
    }

    override suspend fun getRepoEntity(ownerName: String, repoName: String): RepoEntity? {
        return db.repoDao().getRepo(ownerName, repoName)
    }

    private fun setUntilResetLimitTime(response: Response<ApiRepo>) {
        val serverDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME
        val serverDateTime = LocalDateTime.parse(response.headers()["date"], serverDateFormat)
        val serverEpochSecond = serverDateTime.toEpochSecond(ZoneOffset.UTC)
        val resetDate = response.headers()["x-ratelimit-reset"]?.toLong() ?: untilLimitResetTime
        untilLimitResetTime = resetDate - serverEpochSecond
    }

    override fun getUntilLimitResetTimeSec(): Duration {
        return untilLimitResetTime.seconds
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
        endPeriod: LocalDate
    ): List<Stared> {
        val starredList = periodHelper.getStarred(stargazersItemList)
        return periodHelper.getDataInPeriod(startPeriod, endPeriod, starredList)
    }

    override fun clearMemorySavedStargazers() {
        stargazersItemList.clear()
    }
}