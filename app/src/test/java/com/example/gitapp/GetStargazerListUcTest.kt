package com.example.gitapp

import com.example.domain.domain.use_cases.stargazers.GetStargazerListUseCase
import com.example.gitapp.TestData.Companion.staredList1
import org.junit.Assert
import org.junit.Test

class GetStargazerListUcTest {
    private val getStargazerListUseCase = GetStargazerListUseCase()
    @Test
    fun execute_test() {
        val stargazerList = getStargazerListUseCase.execute(staredList1)
        Assert.assertEquals(stargazerList.size, 5)
    }
}