package com.example.gitapp.data.database

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.data.database.AppDatabase
import com.example.data.data.database.dao.RepoDao
import com.example.data.data.database.dao.StargazerDao
import com.example.gitapp.data.database.TestData.Companion.listRepoEntity
import com.example.gitapp.data.database.TestData.Companion.repoEntity1
import com.example.gitapp.data.database.TestData.Companion.repoEntity5
import com.example.gitapp.data.database.TestData.Companion.stargazerList
import com.example.gitapp.data.database.TestData.Companion.userEntity1
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var db: AppDatabase
    private lateinit var repoDao: RepoDao
    private lateinit var stargazerDao: StargazerDao

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