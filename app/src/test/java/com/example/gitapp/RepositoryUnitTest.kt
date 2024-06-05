package com.example.gitapp

import com.example.data.data.RepositoryImpl
import com.example.data.data.api.GitApiService
import com.example.data.data.api.models.ApiRepo
import com.example.data.data.database.AppDatabase
import com.example.domain.domain.use_cases.diagram.ConvertPeriodUseCase
import com.example.gitapp.TestData.Companion.listAdditionalRepo
import com.example.gitapp.TestData.Companion.listAdditionalRepoEntity
import com.example.gitapp.TestData.Companion.listDbRepoEntity
import com.example.gitapp.TestData.Companion.repo3
import com.example.gitapp.TestData.Companion.repoEntity1
import com.example.gitapp.TestData.Companion.repoEntity2
import com.example.gitapp.TestData.Companion.stargazer1
import com.example.gitapp.TestData.Companion.stargazer4
import com.example.gitapp.TestData.Companion.stargazerEntityList
import com.example.gitapp.TestData.Companion.stargazersListTest1
import io.mockk.MockKException
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.Headers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.time.LocalDate

class RepositoryUnitTest {
    private lateinit var repository: RepositoryImpl
    private lateinit var apiService: GitApiService
    private lateinit var convertPeriodUseCase: ConvertPeriodUseCase

    @Before
    fun initRepository() {
        apiService = mockk<GitApiService>()
        convertPeriodUseCase = mockk<ConvertPeriodUseCase>()
        listDbRepoEntity = listOf(repoEntity1, repoEntity2)
        val appDatabase = mockk<AppDatabase>()
        AppDatabase.db = appDatabase
        repository = RepositoryImpl(apiService, convertPeriodUseCase)
    }

    @Test
    fun getOwnerRepoList_responseNotEmpty() {
        every {
            runBlocking {
                apiService.fetchOwnerRepos("", 1)
            }
        } returns listAdditionalRepo

        every {
            runBlocking {
                repository.db.repoDao().isFavoriteRepo(any(), any())
            }
        } returns false

        every {
            listDbRepoEntity = listDbRepoEntity + listAdditionalRepoEntity
            runBlocking {
                repository.db.repoDao().insertAllRepos(any())
            }
        } returns Unit

        every {
            runBlocking {
                repository.db.repoDao().getOwnerRepoList(any())
            }
        } returns listDbRepoEntity.toMutableList()

        runBlocking {
            val assertedRepoList = repository.getOwnerRepoList("", 1)
            Assert.assertEquals(assertedRepoList.size, listDbRepoEntity.size)
        }
    }

    @Test
    fun getOwnerRepoList_responseEmpty() {
        every {
            runBlocking {
                apiService.fetchOwnerRepos("", 1)
            }
        } returns listOf()

        every {
            runBlocking {
                repository.db.repoDao().isFavoriteRepo(any(), any())
            }
        } returns false

        every {
            runBlocking {
                repository.db.repoDao().insertAllRepos(any())
            }
        } returns Unit

        every {
            runBlocking {
                repository.db.repoDao().getOwnerRepoList(any())
            }
        } returns listDbRepoEntity.toMutableList()

        runBlocking {
            val assertedRepoList = repository.getOwnerRepoList("", 1)
            Assert.assertEquals(assertedRepoList.size, listDbRepoEntity.size)
        }
    }

    @Test
    fun getStargazersList_responseNotEmpty() {
        every {
            runBlocking {
                apiService.fetchRepoStarred(
                    ownerName = any(),
                    repository = any(),
                    page = any()
                )
            }
        } returns stargazersListTest1

        every {
            runBlocking {
                repository.db.stargazerDao().insertAll(any())
            }
        } returns Unit

        runBlocking {
            val stargazersList = repository.getStargazersList("", "", 1)
            var loadedStargazers = repository.getLoadedStargazers()
            val firstLoadedStargazerDate = repository.getFirstLoadedStargazerDate()
            val lastLoadedStargazerDate = repository.getLastLoadedStargazerDate()
            Assert.assertEquals(stargazersList.size, stargazersListTest1.size)
            Assert.assertEquals(stargazersList[0].time, stargazersListTest1[3].time)
            Assert.assertEquals(stargazersList[1].time, stargazersListTest1[2].time)
            Assert.assertEquals(stargazersList[2].time, stargazersListTest1[1].time)
            Assert.assertEquals(stargazersList[3].time, stargazersListTest1[0].time)
            Assert.assertEquals(loadedStargazers.size, stargazersListTest1.size)
            Assert.assertEquals(firstLoadedStargazerDate, stargazer4.time)
            Assert.assertEquals(lastLoadedStargazerDate, stargazer1.time)

            repository.clearMemorySavedStargazers()
            loadedStargazers = repository.getLoadedStargazers()
            Assert.assertEquals(loadedStargazers.size, 0)
        }
    }

    @Test
    fun getStargazersList_responseEmpty() {
        every {
            runBlocking {
                apiService.fetchRepoStarred(
                    ownerName = any(),
                    repository = any(),
                    page = any()
                )
            }
        } returns listOf()

        every {
            runBlocking {
                repository.db.stargazerDao().insertAll(any())
            }
        } returns Unit

        runBlocking {
            val stargazersList = repository.getStargazersList("", "", 1)
            Assert.assertEquals(stargazersList.size, 0)
        }
    }

    @Test
    fun getStargazersList() {
        every {
            runBlocking { repository.db.stargazerDao().getStargazers(any(), any()) }
        } returns stargazerEntityList

        runBlocking {
            val dbStargazersList = repository.getStargazersList("", "")
            Assert.assertEquals(dbStargazersList.size, stargazerEntityList.size)
        }
    }

    @Test
    fun testDbFun() {
        every {
            runBlocking { repository.db.repoDao().updateRepoFavorite(any(), any(), any()) }
        } returns Unit
        every {
            runBlocking { repository.db.repoDao().isFavoriteRepo(any(), any()) }
        } returns false
        every {
            runBlocking { repository.db.repoDao().getFavoriteRepoList() }
        } returns listDbRepoEntity
        every {
            runBlocking { repository.db.repoDao().getRepo(any(), any()) }
        } returns repoEntity1
        every {
            runBlocking { repository.db.repoDao().updateRepoStargazersCount(any(), any(), any()) }
        } returns Unit

        runBlocking {
            repository.updateRepoFavorite("", "", false)
            repository.isFavoriteRepo("", "")
            repository.getFavoriteRepoList()
            repository.getRepoEntity("", "")
            repository.updateRepoStargazersCount("", "", 100)
        }
    }

    @Test
    fun getRepoFromApi_responseError() {
        val headers = Headers.Builder()
            .add("date", "Mon, 03 Jun 2024 09:43:56 GMT")
            .add("x-ratelimit-reset", "1717411435")
            .build()
        val errorResponse = mockk<Response<ApiRepo>>()
        every { errorResponse.isSuccessful } returns false
        every { errorResponse.headers() } returns headers
        every {
            runBlocking {
                apiService.fetchOwnerRepo(any(), any())
            }
        } returns errorResponse

        Assert.assertThrows(MockKException::class.java) {
            runBlocking {
                repository.getRepoFromApi("", "")
            }
        }

        val resetTime = repository.getUntilLimitResetTimeSec().inWholeSeconds
        Assert.assertEquals(resetTime, 3599)
    }

    @Test
    fun getRepoFromApi_responseSuccess() {
        val response = mockk<Response<ApiRepo>>()
        every { response.isSuccessful } returns true
        every {
            runBlocking {
                apiService.fetchOwnerRepo(any(), any())
            }
        } returns response
        every { response.body() } returns repo3

        runBlocking {
            val repo = repository.getRepoFromApi("", "")
            Assert.assertEquals(repo?.name, repo3.name)
            Assert.assertEquals(repo?.owner?.nameUser, repo3.owner.nameUser)
        }
    }

    @Test
    fun testConvertUCFun() {
        every { repository.getLoadedDataInPeriod(any(), any()) } returns listOf()
        repository.getLoadedDataInPeriod(LocalDate.now(), LocalDate.now())
    }
}