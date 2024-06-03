package com.example.gitapp

import com.example.data.data.api.models.ApiRepo
import com.example.data.data.api.models.ApiStargazer
import com.example.data.data.api.models.ApiUser
import com.example.domain.domain.models.RepoEntity
import com.example.domain.domain.models.StargazerEntity
import com.example.domain.domain.models.UserEntity
import java.time.LocalDate

class TestData {
    companion object {
        val user1 = ApiUser("google", "https://l.jpg")
        val user2 = ApiUser("roman", "https://l.jpg")
        val user3 = ApiUser("gleb", "https://l.jpg")
        val user4 = ApiUser("sara", "https://l.jpg")
        val user5 = ApiUser("love", "https://l.jpg")

        val userEntity1 = UserEntity("google", "https://l.jpg")
        val userEntity2 = UserEntity("roman", "https://l.jpg")
        val userEntity3 = UserEntity("gleb", "https://l.jpg")
        val userEntity4 = UserEntity("sara", "https://l.jpg")
        val userEntity5 = UserEntity("love", "https://l.jpg")

        val repo3 = ApiRepo(name = "log", user1, 300)
        val repo4 = ApiRepo(name = "full", user1, 0)
        val listAdditionalRepo: List<ApiRepo> = listOf(repo3, repo4)

        val repoEntity1 = RepoEntity(name = "accompanist", userEntity1, 200)
        val repoEntity2 = RepoEntity(name = "all", userEntity1, 10)
        val repoEntity3 = RepoEntity(name = "log", userEntity1, 300)
        val repoEntity4 = RepoEntity(name = "full", userEntity1, 0)
        val listAdditionalRepoEntity: List<RepoEntity> = listOf(repoEntity3, repoEntity4)
        var listDbRepoEntity: List<RepoEntity> = listOf(repoEntity1, repoEntity2)

        val stargazer1 = ApiStargazer(LocalDate.parse("2024-05-27"), user2)
        val stargazer2 = ApiStargazer(LocalDate.parse("2024-04-20"), user3)
        val stargazer3 = ApiStargazer(LocalDate.parse("2024-04-15"), user4)
        val stargazer4 = ApiStargazer(LocalDate.parse("2024-03-10"), user5)
        val stargazersListTest = listOf(stargazer1, stargazer2, stargazer3, stargazer4)

        val stargazerEntity1 = StargazerEntity(LocalDate.parse("2024-05-27"), userEntity2, "full", "roman")
        val stargazerEntity2 = StargazerEntity(LocalDate.parse("2024-04-20"), userEntity3, "full", "roman")
        val stargazerEntity3 = StargazerEntity(LocalDate.parse("2024-04-15"), userEntity4, "full", "roman")
        val stargazerEntity4 = StargazerEntity(LocalDate.parse("2024-03-10"), userEntity5, "full", "roman")
        val stargazerEntityList = listOf(stargazerEntity1, stargazerEntity2, stargazerEntity3, stargazerEntity4)
    }
}