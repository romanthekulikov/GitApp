package com.example.gitapp

import com.example.domain.domain.use_cases.diagram.ConvertPeriodUseCase
import com.example.gitapp.TestData.Companion.date20
import com.example.gitapp.TestData.Companion.date21
import com.example.gitapp.TestData.Companion.date22
import com.example.gitapp.TestData.Companion.date3
import com.example.gitapp.TestData.Companion.date4
import com.example.gitapp.TestData.Companion.date8
import com.example.gitapp.TestData.Companion.staredList1
import com.example.gitapp.TestData.Companion.staredList2
import com.example.gitapp.TestData.Companion.staredList3
import com.example.gitapp.TestData.Companion.stargazersListTest2
import org.junit.Assert
import org.junit.Test

class ConvertPeriodUcTest {
    val convertPeriodUseCase = ConvertPeriodUseCase()
    @Test
    fun getPeriodString() {
        var stringPeriod = convertPeriodUseCase.getPeriodString(staredList1, "week")
        Assert.assertEquals(stringPeriod, "2024-05-27")

        stringPeriod = convertPeriodUseCase.getPeriodString(staredList2, "month")
        Assert.assertEquals(stringPeriod, "2024-05-27 <-> 2024-06-02")

        stringPeriod = convertPeriodUseCase.getPeriodString(staredList3, "year")
        Assert.assertEquals(stringPeriod, "2023-01-01 <-> 2023-01-31")
    }

    @Test
    fun getStarred() {
        val stared = convertPeriodUseCase.getStarred(stargazersListTest2)
        Assert.assertEquals(stared.size, 3)
        var filteredStared = stared.filter { it.time.toString() == "2024-05-27" }
        Assert.assertEquals(filteredStared[0].users.size, 2)
        filteredStared = stared.filter { it.time.toString() == "2024-04-20" }
        Assert.assertEquals(filteredStared[0].users.size, 1)
        filteredStared = stared.filter { it.time.toString() == "2024-04-15" }
        Assert.assertEquals(filteredStared[0].users.size, 1)
    }

    @Test
    fun getDataInPeriod() {
        var periodStarred = convertPeriodUseCase.getDataInPeriod(date3, date4, staredList2)
        Assert.assertEquals(periodStarred.size, 2)

        periodStarred = convertPeriodUseCase.getDataInPeriod(date21, date20, staredList3)
        Assert.assertEquals(periodStarred.size, 12)
    }

    @Test
    fun getMonthWeekPeriodArray() {
        val weekLabelList = convertPeriodUseCase.getMonthWeekPeriodArray(date8, date22)
        Assert.assertEquals(weekLabelList[0], "1-2")
        Assert.assertEquals(weekLabelList[1], "3-9")
        Assert.assertEquals(weekLabelList[2], "10-16")
        Assert.assertEquals(weekLabelList[3], "17-23")
        Assert.assertEquals(weekLabelList[4], "24-30")
    }
}