package com.example.gitapp.data.database

import com.example.domain.domain.models.RepoEntity
import com.example.domain.domain.models.StargazerEntity
import com.example.domain.domain.models.UserEntity
import java.time.LocalDate

class TestData {
    companion object {
        val userEntity1 = UserEntity("google", "https://l.jpg")
        val userEntity2 = UserEntity("roman", "https://l.jpg")
        val userEntity3 = UserEntity("gleb", "https://l.jpg")
        val userEntity4 = UserEntity("sara", "https://l.jpg")
        val userEntity5 = UserEntity("love", "https://l.jpg")

        val repoEntity1 = RepoEntity(name = "accompanist", userEntity1, 200)
        val repoEntity2 = RepoEntity(name = "all", userEntity1, 10)
        val repoEntity3 = RepoEntity(name = "log", userEntity1, 300)
        val repoEntity4 = RepoEntity(name = "full", userEntity1, 0)
        val repoEntity5 = RepoEntity(name = "full", userEntity2, 200)
        val listRepoEntity: List<RepoEntity> = listOf(repoEntity1, repoEntity2, repoEntity3, repoEntity4)

        val stargazer1 = StargazerEntity(LocalDate.parse("2024-05-27"), userEntity2, "full", "roman")
        val stargazer2 = StargazerEntity(LocalDate.parse("2024-04-20"), userEntity3, "full", "roman")
        val stargazer3 = StargazerEntity(LocalDate.parse("2024-04-15"), userEntity4, "full", "roman")
        val stargazer4 = StargazerEntity(LocalDate.parse("2024-03-10"), userEntity5, "full", "roman")
        val stargazerList = listOf(stargazer1, stargazer2, stargazer3, stargazer4)
    }
}