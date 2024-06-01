package com.example.gitapp.data.database

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.data.database.AppDatabase
import com.example.data.data.database.dao.RepoDao
import com.example.data.data.database.dao.StargazerDao
import com.example.domain.domain.models.RepoEntity
import com.example.domain.domain.models.StargazerEntity
import com.example.domain.domain.models.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var db: AppDatabase
    private lateinit var repoDao: RepoDao
    private lateinit var stargazerDao: StargazerDao

    private val userEntity1 = UserEntity("google", "https://l.jpg")
    private val userEntity2 = UserEntity("roman", "https://l.jpg")
    private val userEntity3 = UserEntity("gleb", "https://l.jpg")
    private val userEntity4 = UserEntity("sara", "https://l.jpg")
    private val userEntity5 = UserEntity("love", "https://l.jpg")

    private val repoEntity1 = RepoEntity(name = "accompanist", userEntity1, 200)
    private val repoEntity2 = RepoEntity(name = "all", userEntity1, 10)
    private val repoEntity3 = RepoEntity(name = "log", userEntity1, 300)
    private val repoEntity4 = RepoEntity(name = "full", userEntity1, 0)
    private val repoEntity5 = RepoEntity(name = "full", userEntity2, 200)
    private val listRepoEntity: List<RepoEntity> = listOf(repoEntity1, repoEntity2, repoEntity3, repoEntity4)

    private val stargazer1 = StargazerEntity(LocalDate.parse("2024-05-27"), userEntity2, "full", "roman")
    private val stargazer2 = StargazerEntity(LocalDate.parse("2024-04-20"), userEntity3, "full", "roman")
    private val stargazer3 = StargazerEntity(LocalDate.parse("2024-04-15"), userEntity4, "full", "roman")
    private val stargazer4 = StargazerEntity(LocalDate.parse("2024-03-10"), userEntity5, "full", "roman")
    private val stargazerList = listOf(stargazer1, stargazer2, stargazer3, stargazer4)

    @Before
    fun initDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        repoDao = db.repoDao()
        stargazerDao = db.stargazerDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun checkInsertRepo() {
        runBlocking {
            repoDao.insertAllRepos(listRepoEntity)
            repoDao.insertAllRepos(listRepoEntity)
            val assertedList = repoDao.getOwnerRepoList(userEntity1.nameUser)
            Assert.assertEquals(assertedList.size, listRepoEntity.size)
            assertedList.forEachIndexed { index, repo ->
                Assert.assertEquals(repo.name, listRepoEntity[index].name)
                Assert.assertEquals(repo.stargazersCount, listRepoEntity[index].stargazersCount)
                Assert.assertEquals(repo.owner.nameUser, listRepoEntity[index].owner.nameUser)
            }
        }
    }

    @Test
    fun checkGetRepo() {
        runBlocking {
            var repo = repoDao.getRepo(repoEntity1.owner.nameUser, repoEntity1.name)
            Assert.assertEquals(repo, null)
            repoDao.insertAllRepos(listRepoEntity)
            repo = repoDao.getRepo(repoEntity1.owner.nameUser, repoEntity1.name)
            Assert.assertEquals(repo!!.name, repoEntity1.name)
            Assert.assertEquals(repo.stargazersCount, 200)
        }
    }

    @Test
    fun checkUpdateRepoFavorite() {
        runBlocking {
            repoDao.insertAllRepos(listRepoEntity)
            var favoriteList = repoDao.getFavoriteRepoList()
            Assert.assertThrows(IndexOutOfBoundsException::class.java) {
                runBlocking { favoriteList[0] }
            }
            var repo = repoDao.getRepo(repoEntity1.owner.nameUser, repoEntity1.name)
            Assert.assertEquals(repo?.isFavorite, false)
            repoDao.updateRepoFavorite(repoEntity1.owner.nameUser, repoEntity1.name, true)
            repoDao.updateRepoFavorite("love", "accompanist", true)
            repoDao.updateRepoFavorite(repoEntity1.owner.nameUser, repoEntity1.name, true)
            repo = repoDao.getRepo(repoEntity1.owner.nameUser, repoEntity1.name)
            Assert.assertEquals(repo?.isFavorite, true)

            favoriteList = repoDao.getFavoriteRepoList()
            Assert.assertEquals(favoriteList.size, 1)
            Assert.assertEquals(repoDao.isFavoriteRepo(repoEntity1.owner.nameUser, repoEntity1.name), true)

            repoDao.updateRepoFavorite(repoEntity1.owner.nameUser, repoEntity1.name, false)
            repo = repoDao.getRepo(repoEntity1.owner.nameUser, repoEntity1.name)
            Assert.assertEquals(repo?.isFavorite, false)
            favoriteList = repoDao.getFavoriteRepoList()
            Assert.assertEquals(favoriteList.size, 0)
            Assert.assertEquals(repoDao.isFavoriteRepo(repoEntity1.owner.nameUser, repoEntity1.name), false)
        }
    }

    @Test
    fun checkInsertStargazers() {
        runBlocking {
            Assert.assertThrows(SQLiteConstraintException::class.java) {
                runBlocking { stargazerDao.insertAll(stargazerList) }
            }
            repoDao.insertAllRepos(listOf(repoEntity5))
            stargazerDao.insertAll(stargazerList)
            stargazerDao.insertAll(stargazerList)
            val assertedStargazerList = stargazerDao.getStargazers("full", "roman")
            Assert.assertEquals(stargazerList.size, assertedStargazerList.size)
            assertedStargazerList.forEach { stargazer ->
                val expectedStargazer = stargazerList.find {
                    it.time == stargazer.time && it.user.nameUser == stargazer.user.nameUser
                }
                if (expectedStargazer == null) {
                    Assert.assertEquals(0, 1)
                } else {
                    Assert.assertEquals(expectedStargazer.repoName, stargazer.repoName)
                    Assert.assertEquals(expectedStargazer.ownerName, stargazer.ownerName)
                }
            }
        }
    }
}