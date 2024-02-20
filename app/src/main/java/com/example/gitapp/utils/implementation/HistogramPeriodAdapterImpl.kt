package com.example.gitapp.utils.implementation

import com.example.gitapp.entity.Stared
import com.example.gitapp.ui.diagram.PeriodType
import com.example.gitapp.utils.HistogramPeriodAdapter
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class HistogramPeriodAdapterImpl @Inject constructor() : HistogramPeriodAdapter {

    override fun periodToBarData(
        periodData: List<Stared>,
        periodType: PeriodType,
        startPeriod: LocalDate,
        endPeriod: LocalDate
    ): BarData {
        val values = arrayListOf<BarEntry>()
        var startDay = startPeriod

        var lastDay = when (periodType) {
            PeriodType.WEEK -> startDay
            PeriodType.MONTH -> startDay.with(DayOfWeek.SUNDAY)
            PeriodType.YEAR -> startDay.withDayOfMonth(startDay.lengthOfMonth())
        }
        var entryIndex = 0
        while (lastDay != endPeriod) {
            if (lastDay > endPeriod) {
                lastDay = endPeriod
            }
            val stared = periodData.filter { it.time in startDay..lastDay }.sortedBy { it.time }
            values.add(BarEntry(entryIndex.toFloat(), stared.sumOf { it.users.size }.toFloat(), stared))

            when (periodType) {
                PeriodType.WEEK -> {
                    startDay = startDay.plusDays(1)
                    lastDay = startDay
                }

                PeriodType.MONTH -> {
                    startDay = startDay.plusWeeks(1).with(DayOfWeek.MONDAY)
                    if (lastDay != endPeriod) lastDay = startDay.with(DayOfWeek.SUNDAY)
                }

                PeriodType.YEAR -> {
                    startDay = startDay.plusMonths(1).withDayOfMonth(1)
                    if (lastDay != endPeriod) lastDay = startDay.withDayOfMonth(startDay.lengthOfMonth())
                }
            }

            entryIndex++
        }

        val set = BarDataSet(values, "[$startPeriod]<->[$endPeriod]")
        return BarData(set)
    }
}