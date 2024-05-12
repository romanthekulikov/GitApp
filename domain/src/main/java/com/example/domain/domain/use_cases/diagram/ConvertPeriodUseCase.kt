package com.example.domain.domain.use_cases.diagram

import com.example.domain.domain.entity.Stared
import com.example.domain.domain.entity.Stargazer
import com.example.domain.domain.entity.User
import com.example.domain.domain.models.StaredModel
import java.time.DayOfWeek
import java.time.LocalDate

class ConvertPeriodUseCase {

    /**
     * Return date diapason for starred data. Return string such as "2024-03-29" or
     * "2024-03-25 <-> 2024-03-31" relatively PeriodType
     */
    fun getPeriodString(partData: List<Stared>, periodType: String): String {
        var startPeriodPart = partData[0].time
        return when (periodType) {
            WEEK -> {
                startPeriodPart.toString() //Just one day
            }

            MONTH -> {
                val endPeriodPartLocalDate = startPeriodPart.with(DayOfWeek.SUNDAY)
                startPeriodPart = endPeriodPartLocalDate.with(DayOfWeek.MONDAY)
                "$startPeriodPart <-> $endPeriodPartLocalDate"
            }

            YEAR -> {
                val endPeriodPart = startPeriodPart.withDayOfMonth(startPeriodPart.lengthOfMonth())
                startPeriodPart = endPeriodPart.withDayOfMonth(1)
                "$startPeriodPart <-> $endPeriodPart"
            }

            else -> throw IllegalArgumentException(EXCEPTION_ILLEGAL_PERIOD)
        }
    }

    /**
     * Sorts stargazers into Stared by stargazer time
     */
    fun getStarred(stargazersItemsList: List<Stargazer>): List<Stared> {
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

    /**
     * Return part of data in specified period
     */
    fun getDataInPeriod(startPeriod: LocalDate, endPeriod: LocalDate, staredList: List<Stared>): List<Stared> {
        return staredList.filter { it.time in startPeriod..endPeriod }
    }

    /**
     * Return array week period in specified period.
     * String such as 25-31, when 25 is Monday and 31 is Sunday
     */
    fun getMonthWeekPeriodArray(startPeriod: LocalDate, endPeriod: LocalDate): Array<String> {
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