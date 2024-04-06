package com.example.domain.domain

import com.example.domain.domain.entity.Stared
import com.example.domain.domain.entity.Stargazer
import com.example.domain.domain.entity.User
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class PeriodHelperImpl @Inject constructor() : PeriodHelper {
    override fun getPeriodString(partData: List<Stared>, periodType: String): String {
        var startPeriodPart = partData[0].time
        return when (periodType) {
            "week" -> {
                startPeriodPart.toString() //Just one day
            }

            "month" -> {
                val endPeriodPartLocalDate = startPeriodPart.with(DayOfWeek.SUNDAY)
                startPeriodPart = endPeriodPartLocalDate.with(DayOfWeek.MONDAY)
                "$startPeriodPart <-> $endPeriodPartLocalDate"
            }

            "year" -> {
                val endPeriodPart = startPeriodPart.withDayOfMonth(startPeriodPart.lengthOfMonth())
                startPeriodPart = endPeriodPart.withDayOfMonth(1)
                "$startPeriodPart <-> $endPeriodPart"
            }
            else -> throw IllegalArgumentException("Illegal period type found")
        }
    }

    override fun getStarred(stargazersItemsList: List<Stargazer>): List<Stared> {
        val starredList = mutableListOf<Stared>()
        val tempStargazersList = stargazersItemsList.toMutableList()
        var listUser: List<User>

        while (tempStargazersList.size > 0) {
            val stargazer = tempStargazersList[0]
            val equalTimeStargazersList = tempStargazersList.filter { it.time == stargazer.time }
            listUser = equalTimeStargazersList.map { it.user }
            starredList.add(StaredModel(stargazer.time, listUser))
            tempStargazersList.removeAll(equalTimeStargazersList)
        }

        return starredList
    }

    override fun getDataInPeriod(startPeriod: LocalDate, endPeriod: LocalDate, staredList: List<Stared>): List<Stared> {
        return staredList.filter { it.time in startPeriod..endPeriod }
    }

    override fun getMonthWeekPeriodArray(startPeriod: LocalDate, endPeriod: LocalDate): Array<String> {
        val weekPeriodArray = arrayListOf<String>()
        var startDayWeek = startPeriod
        var lastDayWeek = startDayWeek.with(DayOfWeek.SUNDAY)
        while (lastDayWeek != endPeriod) {
            weekPeriodArray.add("${startDayWeek.dayOfMonth}-${lastDayWeek.dayOfMonth}")
            startDayWeek = lastDayWeek.plusWeeks(1).with(DayOfWeek.MONDAY)
            lastDayWeek = startDayWeek.with(DayOfWeek.SUNDAY)
            if (lastDayWeek > endPeriod) {
                lastDayWeek = endPeriod
            }
        }
        weekPeriodArray.add("${startDayWeek.dayOfMonth}-${lastDayWeek.dayOfMonth}")
        return weekPeriodArray.toTypedArray()
    }
}