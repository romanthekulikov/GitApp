package com.example.gitapp.ui.service_repo

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.data.data.repository.Repository
import com.example.domain.domain.entity.Repo
import com.example.gitapp.App
import com.example.gitapp.R
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

const val REPO_INFO_SEPARATOR = "/"
const val START_CHANNEL_ID = "start_repo_channel"
const val START_CHANNEL_NAME = "start_notification_channel"
const val START_NOTIFICATION_ID = 123

class RepoWorker(private val context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var repoNotificationCreator: NotificationsCreator<Repo>

    private var workerResult = Result.success()

    init {
        App.appComponent.inject(this)
    }

    override suspend fun doWork(): Result {
        initForegroundWork()

        val repoList = getRepoForUpdates()
        val differenceRepoList = getDifferenceRepoList(repoList) // has repository with difference stargazersCount
        repoNotificationCreator.showNotifications(differenceRepoList)

        return workerResult
    }

    private fun initForegroundWork() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        repoNotificationCreator.createNotificationChannel(
            notificationManager = notificationManager,
            id = START_CHANNEL_ID,
            name = START_CHANNEL_NAME,
            description = context.getString(R.string.start_repo_channel_description)
        )
        val startNotification = NotificationCompat.Builder(context, START_CHANNEL_ID)
            .setChannelId(START_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .build()

        setForegroundAsync(ForegroundInfo(START_NOTIFICATION_ID, startNotification))
    }

    private suspend fun getRepoForUpdates(): List<Repo>? {
        val repos = inputData.getStringArray("repos")
        val repoList = mutableListOf<Repo>()
        repos?.forEach { repoData ->
            try {
                val repoSplitData = repoData.split(REPO_INFO_SEPARATOR)
                val ownerName = repoSplitData[0]
                val repoName = repoSplitData[1]
                val repo = repository.getRepoEntity(ownerName, repoName)
                if (repo != null) {
                    repoList.add(repo)
                }
            } catch (_: ArrayIndexOutOfBoundsException) { /*nothing*/
            }
        }

        return if (repoList.isEmpty()) null else repoList
    }

    private suspend fun getDifferenceRepoList(repoList: List<Repo>?): List<Repo> {
        val favoriteRepos = repoList ?: repository.getFavoriteRepoList()
        val differenceRepoList = mutableListOf<Repo>()
        favoriteRepos.forEachIndexed { index, repo ->
            try {
                if (differenceRepoList.size > 5) throw IOException()
                val fetchedRepo = repository.getRepoFromApi(repo.owner.nameUser, repo.name)!!
                fetchedRepo.stargazersCount = fetchedRepo.stargazersCount - repo.stargazersCount
                differenceRepoList.add(fetchedRepo)
            } catch (e: IOException) {
                changeWorkerResultWithData(e, favoriteRepos.subList(index, favoriteRepos.size), Result.failure())
                return differenceRepoList
            } catch (e: HttpException) {
                changeWorkerResultWithData(e, favoriteRepos.subList(index, favoriteRepos.size), Result.failure())
                return differenceRepoList
            }
        }

        changeWorkerResultWithData(repoForUpdates = listOf(), result = Result.success())
        RepoWorkerHelper.initWorker(context)
        return differenceRepoList
    }

    private fun changeWorkerResultWithData(e: Exception? = null, repoForUpdates: List<Repo>, result: Result) {
        val startAfterSec = repository.getUntilLimitResetTimeSec().inWholeSeconds
        val reposForUpdate = convertRepoListToStringList(repoForUpdates)
        val data = RepoWorkerHelper.getWorkerData(reposForUpdate, startAfterSec)

        workerResult = if (result == Result.failure()) {
            Log.e("service_error", "Service Exception: ", e)
            Result.failure(data)
        } else {
            Result.success(data)
        }
    }

    private fun convertRepoListToStringList(repoList: List<Repo>): List<String> {
        val stringList = mutableListOf<String>()
        repoList.forEach { repo ->
            stringList.add("${repo.owner.nameUser}$REPO_INFO_SEPARATOR${repo.name}")
        }

        return stringList
    }
}