package com.example.gitapp.utils.implementation

import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.ui.diagram.models.Month
import com.example.gitapp.ui.diagram.models.Week
import com.example.gitapp.ui.diagram.models.Year
import com.example.gitapp.utils.PeriodHelper
import java.time.DayOfWeek
import java.time.LocalDate

class PeriodHelperImpl : PeriodHelper {
    override fun getWeekStargazerByPeriod(
        startPeriod: LocalDate,
        endPeriod: LocalDate,
        listPageItemsStargazers: MutableList<ApiStarredData>
    ): Week {
        val week = Week()
        for (i in listPageItemsStargazers.indices) {
            val stargazer = listPageItemsStargazers[i]
            val stargazerDate = stargazer.getLocalDate()
            if (stargazerDate in startPeriod..endPeriod) {
                when (stargazerDate.dayOfWeek) {
                    DayOfWeek.TUESDAY -> week.weekDays[1].add(stargazer)
                    DayOfWeek.WEDNESDAY -> week.weekDays[2].add(stargazer)
                    DayOfWeek.THURSDAY -> week.weekDays[3].add(stargazer)
                    DayOfWeek.FRIDAY -> week.weekDays[4].add(stargazer)
                    DayOfWeek.SATURDAY -> week.weekDays[5].add(stargazer)
                    DayOfWeek.SUNDAY -> week.weekDays[6].add(stargazer)
                    else -> week.weekDays[0].add(stargazer)
                }
            }
        }
        return week
    }

    override fun getMonthStargazerByPeriod(
        startPeriod: LocalDate,
        endPeriod: LocalDate,
        listPageItemsStargazers: MutableList<ApiStarredData>
    ): Month {
        val month = Month()
        var firstDayWeek = startPeriod
        while (firstDayWeek < endPeriod) {
            var lastDayWeek = firstDayWeek.with(DayOfWeek.SUNDAY)
            if (lastDayWeek > endPeriod) {//Last weak on the month
                lastDayWeek = endPeriod
            }
            val week = getWeekStargazerByStringPeriod(startPeriod.toString(), lastDayWeek.toString(), listPageItemsStargazers)
            month.weeks.add(week)
            firstDayWeek = firstDayWeek.plusWeeks(1).with(DayOfWeek.MONDAY)
        }
        return month
    }

    private fun getWeekStargazerByStringPeriod(
        startPeriod: String,
        endPeriod: String,
        listPageItemsStargazers: MutableList<ApiStarredData>
    ): Week {
        val week = Week()
        for (stargazer in listPageItemsStargazers) {
            if (stargazer.time in startPeriod..endPeriod) {
                week.weekDays[0].add(stargazer)
            }
        }
        return week
    }

    override fun getYearStargazerByStartYear(startYear: LocalDate, listPageItemsStargazers: MutableList<ApiStarredData>): Year {
        val year = Year()
        var firstDayMonth = startYear
        for (i in 1..12) {
            val lastDayMonth = firstDayMonth.plusMonths(1).minusDays(1)
            val month = getMonthStargazerByPeriod(firstDayMonth, lastDayMonth, listPageItemsStargazers)
            year.months.add(month)
            firstDayMonth = firstDayMonth.plusMonths(1)
        }
        return year
    }
}