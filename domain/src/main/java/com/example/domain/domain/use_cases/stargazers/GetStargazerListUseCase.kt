package com.example.domain.domain.use_cases.stargazers

import com.example.domain.domain.entity.Stared
import com.example.domain.domain.entity.Stargazer
import com.example.domain.domain.models.StargazerModel
import com.example.domain.domain.models.UserEntity

class GetStargazerListUseCase {
    fun execute(staredData: List<Stared>): MutableList<Stargazer> {
        val stargazerList = mutableListOf<Stargazer>()
        for (starred in staredData) {
            starred.users.forEach { user ->
                stargazerList.add(
                    StargazerModel(
                        time = starred.time,
                        user = UserEntity(user.nameUser, user.avatarUrl)
                    )
                )
            }
        }

        return stargazerList
    }
}