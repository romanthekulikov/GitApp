package com.example.gitapp

import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.data.data.RepositoryImpl
import com.example.data.data.api.GitApiService
import com.example.data.data.api.models.ApiRepo
import com.example.data.data.api.models.ApiUser
import com.example.data.data.converters.LocalDateConverter
import com.example.domain.domain.use_cases.diagram.ConvertPeriodUseCase
import com.example.gitapp.injection.modules.BASE_URL
import com.example.gitapp.ui.main.MainActivity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(AndroidJUnit4::class)
@LargeTest
class RepositoryTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun test(): Unit = runBlocking {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(LocalDateConverter())
            .build()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL).build()
        val apiService = retrofit.create(GitApiService::class.java)
        val repository = RepositoryImpl(apiService, ConvertPeriodUseCase())
        withContext(Dispatchers.IO) {
            var repos = repository.getOwnerRepoList("romanthekulikov", 1)
            var dbRepos = repository.getOwnerRepoList("romanthekulikov")
            Assert.assertEquals(repos.size, 20)
            Assert.assertEquals(dbRepos.size, 20)

            repos = repository.getOwnerRepoList("oty", 1)
            dbRepos = repository.getOwnerRepoList("oty")
            Assert.assertEquals(repos.size, 0)
            Assert.assertEquals(dbRepos.size, 0)

            repository.getOwnerRepoList("Omega-R", 1)
            repository.getOwnerRepoList("Omega-R", 2)
            var stargazers = repository.getStargazersList("Omega-R", "OmegaIntentBuilder", 1)
            var dbStargazers = repository.getStargazersList("Omega-R", "OmegaIntentBuilder")
            var loadedStargazers = repository.getLoadedStargazers()
            Assert.assertEquals(stargazers.size, 41)
            Assert.assertEquals(dbStargazers.size, 41)
            Assert.assertEquals(loadedStargazers.size, 41)

            repository.clearMemorySavedStargazers()
            loadedStargazers = repository.getLoadedStargazers()
            Assert.assertEquals(loadedStargazers.size, 0)

            stargazers = repository.getStargazersList("Omega-R", "DropDown", 1)
            dbStargazers = repository.getStargazersList("Omega-R", "DropDown")
            Assert.assertEquals(stargazers.size, 0)
            Assert.assertEquals(dbStargazers.size, 0)

            repository.updateRepoFavorite("Omega-R", "DropDown", true)
            var isFavorite = repository.isFavoriteRepo("Omega-R", "DropDown")
            Assert.assertEquals(isFavorite, true)
            repository.updateRepoFavorite("Omega-R", "DropDown", false)
            isFavorite = repository.isFavoriteRepo("Omega-R", "DropDown")
            Assert.assertEquals(isFavorite, false)

            repository.updateRepoFavorite("Omega-R", "DropDown", true)
            repository.updateRepoFavorite("Omega-R", "OmegaIntentBuilder", true)
            val favoriteCount = repository.getFavoriteRepoList().size
            Assert.assertEquals(favoriteCount, 2)

            val apiRepo = repository.getRepoFromApi("Omega-R", "OmegaIntentBuilder")
            val expectedApiRepo = ApiRepo(name = "OmegaIntentBuilder",
                owner = ApiUser(nameUser = "Omega-R", ""),
                stargazersCount = 41
            )
            Assert.assertEquals(apiRepo!!.stargazersCount, expectedApiRepo.stargazersCount)
            Assert.assertEquals(apiRepo.name, expectedApiRepo.name)
            Assert.assertEquals(apiRepo.owner.nameUser, expectedApiRepo.owner.nameUser)

            repository.updateRepoStargazersCount("Omega-R", "DropDown", 5)
            val repo = repository.getRepoEntity("Omega-R", "DropDown")
            Assert.assertEquals(repo!!.stargazersCount, 5)
        }
    }
}