package com.example.gitapp

import com.example.domain.domain.use_cases.stargazers.GetStargazerListUseCase
import com.example.gitapp.TestData.Companion.listUser1
import com.example.gitapp.TestData.Companion.listUser2
import com.example.gitapp.TestData.Companion.staredList1
import org.junit.Assert
import org.junit.Test

class GetStargazerListUcTest {
    private val getStargazerListUseCase = GetStargazerListUseCase()
    @Test
    fun execute_test() {
        val stargazerList = getStargazerListUseCase.execute(staredList1)
        Assert.assertEquals(stargazerList.size, 5)
        val userList = listUser1 + listUser2

        stargazerList.forEach { stargazer ->
            val t = userList.filter { it.nameUser == stargazer.user.nameUser}
            Assert.assertEquals(t.size, 1)
        }
    }
}