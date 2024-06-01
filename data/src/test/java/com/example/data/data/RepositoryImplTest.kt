package com.example.data.data

import com.example.data.data.api.GitApiService
import com.example.data.data.api.models.ApiRepo
import com.example.data.data.api.models.ApiUser
import com.example.data.data.converters.toRepoEntityList
import com.example.data.data.database.AppDatabase
import com.example.data.data.database.dao.RepoDao
import com.example.domain.domain.models.RepoEntity
import com.example.domain.domain.models.UserEntity
import com.example.domain.domain.use_cases.diagram.ConvertPeriodUseCase
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class RepositoryImplTest {
    private val apiUser = ApiUser("google", "https://l.jpg")
    private val userEntity = UserEntity("google", "https://l.jpg")
    val apiRepo1 = ApiRepo(name = "accompanist", apiUser, 200)
    val apiRepo2 = ApiRepo(name = "all", apiUser, 10)
    val apiRepo3 = ApiRepo(name = "log", apiUser, 300)
    val apiRepo4 = ApiRepo(name = "full", apiUser, 0)
    val listRepos = listOf(apiRepo1, apiRepo2, apiRepo3, apiRepo4)

    val repoEntity1 = RepoEntity(name = "accompanist", userEntity, 200)
    val repoEntity2 = RepoEntity(name = "all", userEntity, 10)
    val repoEntity3 = RepoEntity(name = "log", userEntity, 300)
    val repoEntity4 = RepoEntity(name = "full", userEntity, 0)
    val listRepoEntity: List<RepoEntity> = listOf(repoEntity1, repoEntity2, repoEntity3, repoEntity4)
    @Test
    fun getRepoFromApiTest() {
        val api = spyk<GitApiService>()
        val convertPeriodUC = spyk<ConvertPeriodUseCase>()
        val db = spyk<AppDatabase>()
        val repoDao = spyk<RepoDao>()
        every {
            db.repoDao()
        } returns repoDao

        AppDatabase.db = db
        val repository = RepositoryImpl(api, convertPeriodUC)
        mockkRepoDao(repoDao)
        every {
            runBlocking {
                api.fetchOwnerRepos("google", 1)
            }
        } returns listRepos

        runBlocking {
            val expectedRepoEntityList = listRepos.toRepoEntityList()
            val assertedRepoEntityList = repository.getOwnerRepoList("google", 1)
            expectedRepoEntityList.forEachIndexed { index, repo ->
                Assert.assertEquals(repo.name, assertedRepoEntityList[index].name)
                Assert.assertEquals(repo.stargazersCount, assertedRepoEntityList[index].stargazersCount)
            }
        }
    }

    private fun mockkRepoDao(repoDao: RepoDao) {
        every {
            runBlocking {
                repoDao.isFavoriteRepo("google", "accompanist")
            }
        } returns true
        every {
            runBlocking {
                repoDao.isFavoriteRepo("google", "all")
            }
        } returns true
        every {
            runBlocking {
                repoDao.isFavoriteRepo("google", "log")
            }
        } returns true
        every {
            runBlocking {
                repoDao.isFavoriteRepo("google", "full")
            }
        } returns true
        every {
            runBlocking {
                repoDao.getOwnerRepoList("google")
            }
        } returns listRepoEntity
    }
}