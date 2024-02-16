package com.example.gitapp.utils.implementation

import com.example.gitapp.ui.diagram.DiagramMode
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.utils.PeriodHelper
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class PeriodHelperImpl @Inject constructor() : PeriodHelper {

    override fun getPeriodString(partData: List<ApiStarredData>, diagramMode: DiagramMode): String {
        var startPeriodPart = partData[0].timeString
        val startPeriodPartLocalDate = partData[0].time
        return when (diagramMode) {
            DiagramMode.WEEK -> {
                startPeriodPart //Just one day
            }

            DiagramMode.MONTH -> {
                val endPeriodPartLocalDate = startPeriodPartLocalDate.with(DayOfWeek.SUNDAY)
                startPeriodPart = endPeriodPartLocalDate.with(DayOfWeek.MONDAY).toString()
                "$startPeriodPart <-> $endPeriodPartLocalDate"
            }

            DiagramMode.YEAR -> {
                val endPeriodPart = startPeriodPartLocalDate.withDayOfMonth(startPeriodPartLocalDate.lengthOfMonth())
                startPeriodPart = endPeriodPart.withDayOfMonth(1).toString()
                "$startPeriodPart <-> $endPeriodPart"
            }
        }
    }

    override fun getDataInPeriod(
        startPeriod: LocalDate,
        endPeriod: LocalDate,
        periodType: DiagramMode,
        stargazersItemsList: List<ApiStarredData>
    ): List<List<ApiStarredData>> {
        val values = mutableListOf<List<ApiStarredData>>()
        var startDay = startPeriod

        var lastDay = when (periodType) {
            DiagramMode.WEEK -> startDay
            DiagramMode.MONTH -> startDay.with(DayOfWeek.SUNDAY)
            DiagramMode.YEAR -> startDay.withDayOfMonth(startDay.lengthOfMonth())
        }
        var monthIndex = 0
        while (lastDay != endPeriod) {
            if (lastDay > endPeriod) {
                lastDay = endPeriod
            }
            val stargazers = stargazersItemsList.filter { it.time in startDay..lastDay }.sortedBy { it.time }
            values.add(stargazers)

            when (periodType) {
                DiagramMode.WEEK -> {
                    startDay = startDay.plusDays(1)
                    lastDay = startDay
                }

                DiagramMode.MONTH -> {
                    startDay = startDay.plusWeeks(1).with(DayOfWeek.MONDAY)
                    if (lastDay != endPeriod) lastDay = startDay.with(DayOfWeek.SUNDAY)
                }

                DiagramMode.YEAR -> {
                    startDay = startDay.plusMonths(1).withDayOfMonth(1)
                    if (lastDay != endPeriod) lastDay = startDay.withDayOfMonth(startDay.lengthOfMonth())
                }
            }

            monthIndex++
        }

        return values
    }
}