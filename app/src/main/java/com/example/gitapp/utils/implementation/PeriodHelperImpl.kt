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

    override fun getDiagramData(
        startPeriod: LocalDate,
        endPeriod: LocalDate,
        diagramMode: DiagramMode,
        stargazersItemsList: List<ApiStarredData>
    ): List<List<ApiStarredData>> {
        val values = mutableListOf<List<ApiStarredData>>()
        var startMonthDay = startPeriod

        var endMonthDay = when (diagramMode) {
            DiagramMode.WEEK -> startMonthDay
            DiagramMode.MONTH -> startMonthDay.with(DayOfWeek.SUNDAY)
            DiagramMode.YEAR -> startMonthDay.withDayOfMonth(startMonthDay.lengthOfMonth())
        }

        var weeksPeriodList = emptyArray<String>()
        var monthIndex = 0
        while (endMonthDay != endPeriod) {
            if (endMonthDay > endPeriod) {
                endMonthDay = endPeriod
            }
            val stargazers = stargazersItemsList.filter { it.time in startMonthDay..endMonthDay }.sortedBy { it.time }
            values.add(stargazers)

            when (diagramMode) {
                DiagramMode.WEEK -> {
                    startMonthDay = startMonthDay.plusDays(1)
                    endMonthDay = startMonthDay
                }

                DiagramMode.MONTH -> {
                    weeksPeriodList = weeksPeriodList.plus("${startMonthDay.dayOfMonth}-${endMonthDay.dayOfMonth}")
                    startMonthDay = startMonthDay.plusWeeks(1).with(DayOfWeek.MONDAY)
                    if (endMonthDay != endPeriod) endMonthDay = startMonthDay.with(DayOfWeek.SUNDAY)
                }

                DiagramMode.YEAR -> {
                    startMonthDay = startMonthDay.plusMonths(1).withDayOfMonth(1)
                    if (endMonthDay != endPeriod) endMonthDay = startMonthDay.withDayOfMonth(startMonthDay.lengthOfMonth())
                }
            }

            monthIndex++
        }

        return values
    }
}