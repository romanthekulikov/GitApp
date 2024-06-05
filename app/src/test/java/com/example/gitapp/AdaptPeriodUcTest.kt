package com.example.gitapp

import android.graphics.Color
import com.example.domain.domain.entity.Stared
import com.example.domain.domain.use_cases.diagram.AdaptPeriodUseCase
import com.example.gitapp.TestData.Companion.date1
import com.example.gitapp.TestData.Companion.date20
import com.example.gitapp.TestData.Companion.date21
import com.example.gitapp.TestData.Companion.date3
import com.example.gitapp.TestData.Companion.date7
import com.example.gitapp.TestData.Companion.date8
import com.example.gitapp.TestData.Companion.staredList1
import com.example.gitapp.TestData.Companion.staredList2
import com.example.gitapp.TestData.Companion.staredList3
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Suppress("UNCHECKED_CAST")
class AdaptPeriodUcTest {
    private val adaptPeriodUseCase = AdaptPeriodUseCase()

    @Before
    fun initColor() {
        mockkStatic(Color::class)
        every { Color.rgb(any(Int::class), any(Int::class), any(Int::class)) } returns Color.BLUE
    }
    @Test
    fun periodToBarData_week() {
        val barData = adaptPeriodUseCase.periodToBarData(staredList1, "week", date1, date3)

        val mondayStared = barData.dataSets[0].getEntryForIndex(0).data as List<Stared>
        if (mondayStared.isNotEmpty()) {
            Assert.assertEquals(mondayStared[0].users.size, 3)
        } else {
            Assert.assertEquals(0, 1)
        }

        val thursdayStared = barData.dataSets[0].getEntryForIndex(3).data as List<Stared>
        if (thursdayStared.isNotEmpty()) {
            Assert.assertEquals(thursdayStared[0].users.size, 2)
        } else {
            Assert.assertEquals(0, 1)
        }
    }

    @Test
    fun periodToBarData_month() {
        val barData = adaptPeriodUseCase.periodToBarData(staredList2, "month", date8, date7)
        val firstWeekStared = barData.dataSets[0].getEntryForIndex(0).data as List<Stared>
        val secondWeekStared = barData.dataSets[0].getEntryForIndex(1).data as List<Stared>
        val thirdWeekStared = barData.dataSets[0].getEntryForIndex(2).data as List<Stared>
        val fourthWeekStared = barData.dataSets[0].getEntryForIndex(3).data as List<Stared>
        val fifthWeekStared = barData.dataSets[0].getEntryForIndex(4).data as List<Stared>
        val weekStarredList = listOf(firstWeekStared, secondWeekStared, thirdWeekStared, fourthWeekStared, fifthWeekStared)
        for (weekStared in weekStarredList) {
            if (weekStared.isNotEmpty()) {
                Assert.assertEquals(weekStared.size, 1)
            } else {
                Assert.assertEquals(0, 1)
            }
        }
    }

    @Test
    fun periodToBarData_year() {
        val barData = adaptPeriodUseCase.periodToBarData(staredList3, "year", date21, date20)
        for (i in 0..< barData.dataSets[0].entryCount) {
            val monthStared = barData.dataSets[0].getEntryForIndex(i).data as List<Stared>
            if (monthStared.isNotEmpty()) {
                Assert.assertEquals(monthStared.size, 1)
            }
        }
    }

    @Test
    fun periodToBarData_error() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            adaptPeriodUseCase.periodToBarData(staredList3, "faf", date21, date20)
        }
    }
}