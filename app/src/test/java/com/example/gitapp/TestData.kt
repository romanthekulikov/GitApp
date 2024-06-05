package com.example.gitapp

import com.example.data.data.api.models.ApiRepo
import com.example.data.data.api.models.ApiStargazer
import com.example.data.data.api.models.ApiUser
import com.example.domain.domain.models.RepoEntity
import com.example.domain.domain.models.StaredModel
import com.example.domain.domain.models.StargazerEntity
import com.example.domain.domain.models.UserEntity
import java.time.LocalDate

class TestData {
    companion object {
        val userRepository1 = ApiUser("google", "https://l.jpg")
        val userRepository2 = ApiUser("roman", "https://l.jpg")
        val userRepository3 = ApiUser("gleb", "https://l.jpg")
        val userRepository4 = ApiUser("sara", "https://l.jpg")
        val userRepository5 = ApiUser("love", "https://l.jpg")

        val userEntity1 = UserEntity("google", "https://l.jpg")
        val userEntity2 = UserEntity("roman", "https://l.jpg")
        val userEntity3 = UserEntity("gleb", "https://l.jpg")
        val userEntity4 = UserEntity("sara", "https://l.jpg")
        val userEntity5 = UserEntity("love", "https://l.jpg")

        val repo3 = ApiRepo(name = "log", userRepository1, 300)
        val repo4 = ApiRepo(name = "full", userRepository1, 0)
        val listAdditionalRepo: List<ApiRepo> = listOf(repo3, repo4)

        val repoEntity1 = RepoEntity(name = "accompanist", userEntity1, 200)
        val repoEntity2 = RepoEntity(name = "all", userEntity1, 10)
        val repoEntity3 = RepoEntity(name = "log", userEntity1, 300)
        val repoEntity4 = RepoEntity(name = "full", userEntity1, 0)
        val listAdditionalRepoEntity: List<RepoEntity> = listOf(repoEntity3, repoEntity4)
        var listDbRepoEntity: List<RepoEntity> = listOf(repoEntity1, repoEntity2)

        val stargazer1 = ApiStargazer(LocalDate.parse("2024-05-27"), userRepository2)
        val stargazer2 = ApiStargazer(LocalDate.parse("2024-04-20"), userRepository3)
        val stargazer3 = ApiStargazer(LocalDate.parse("2024-04-15"), userRepository4)
        val stargazer4 = ApiStargazer(LocalDate.parse("2024-03-10"), userRepository5)
        val stargazer5 = ApiStargazer(LocalDate.parse("2024-05-27"), userRepository5)
        val stargazersListTest1 = listOf(stargazer1, stargazer2, stargazer3, stargazer4)
        val stargazersListTest2 = listOf(stargazer1, stargazer2, stargazer3, stargazer5)

        val stargazerEntity1 = StargazerEntity(LocalDate.parse("2024-05-27"), userEntity2, "full", "roman")
        val stargazerEntity2 = StargazerEntity(LocalDate.parse("2024-04-20"), userEntity3, "full", "roman")
        val stargazerEntity3 = StargazerEntity(LocalDate.parse("2024-04-15"), userEntity4, "full", "roman")
        val stargazerEntity4 = StargazerEntity(LocalDate.parse("2024-03-10"), userEntity5, "full", "roman")
        val stargazerEntityList = listOf(stargazerEntity1, stargazerEntity2, stargazerEntity3, stargazerEntity4)

        val date1 = LocalDate.parse("2024-05-27")
        val date2 = LocalDate.parse("2024-05-30")
        val date3 = LocalDate.parse("2024-06-02")
        val date4 = LocalDate.parse("2024-06-03")
        val date5 = LocalDate.parse("2024-06-12")
        val date6 = LocalDate.parse("2024-06-21")
        val date7 = LocalDate.parse("2024-06-30")
        val date8 = LocalDate.parse("2024-06-01")
        val date22 = LocalDate.parse("2024-06-30")
        val date9 = LocalDate.parse("2023-01-02")
        val date10 = LocalDate.parse("2023-02-10")
        val date11 = LocalDate.parse("2023-03-08")
        val date12 = LocalDate.parse("2023-04-23")
        val date13 = LocalDate.parse("2023-05-11")
        val date14 = LocalDate.parse("2023-06-19")
        val date15 = LocalDate.parse("2023-07-01")
        val date16 = LocalDate.parse("2023-08-24")
        val date17 = LocalDate.parse("2023-09-30")
        val date18 = LocalDate.parse("2023-10-02")
        val date19 = LocalDate.parse("2023-11-12")
        val date20 = LocalDate.parse("2023-12-31")
        val date21 = LocalDate.parse("2023-01-01")

        val user1 = UserEntity("google", "https://l.jpg")
        val user2 = UserEntity("roman", "https://l.jpg")
        val user3 = UserEntity("gleb", "https://l.jpg")
        val user4 = UserEntity("sara", "https://l.jpg")
        val user5 = UserEntity("love", "https://l.jpg")
        val user6 = UserEntity("Andro", "https://l.jpg")
        val user7 = UserEntity("Kirill", "https://l.jpg")
        val user8 = UserEntity("Sora", "https://l.jpg")
        val user9 = UserEntity("Hell", "https://l.jpg")
        val user10 = UserEntity("Gift", "https://l.jpg")
        val user11 = UserEntity("Little", "https://l.jpg")
        val user12 = UserEntity("Soft", "https://l.jpg")
        val listUser1 = listOf(user1, user2, user3)
        val listUser2 = listOf(user5, user4)

        val stared1 = StaredModel(date1, listUser1)
        val stared2 = StaredModel(date2, listUser2)

        val stared3 = StaredModel(date3, listOf(user1))
        val stared4 = StaredModel(date4, listOf(user2))
        val stared5 = StaredModel(date5, listOf(user3))
        val stared6 = StaredModel(date6, listOf(user4))
        val stared7 = StaredModel(date7, listOf(user5))

        val stared9 = StaredModel(date9, listOf(user1))
        val stared10 = StaredModel(date10, listOf(user2))
        val stared11 = StaredModel(date11, listOf(user3))
        val stared12 = StaredModel(date12, listOf(user4))
        val stared13 = StaredModel(date13, listOf(user5))
        val stared14 = StaredModel(date14, listOf(user6))
        val stared15 = StaredModel(date15, listOf(user7))
        val stared16 = StaredModel(date16, listOf(user8))
        val stared17 = StaredModel(date17, listOf(user9))
        val stared18 = StaredModel(date18, listOf(user10))
        val stared19 = StaredModel(date19, listOf(user11))
        val stared20 = StaredModel(date20, listOf(user12))

        val staredList1 = listOf(stared1, stared2)
        val staredList2 = listOf(stared3, stared4, stared5, stared6, stared7)
        val staredList3 = listOf(stared9, stared10, stared11, stared12, stared13, stared14, stared15, stared16, stared17,
            stared18, stared19, stared20)
    }
}