package com.example.domain.domain

import com.example.domain.domain.entity.Stared
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class HistogramPeriodAdapterImpl @Inject constructor() : HistogramPeriodAdapter {

    override fun periodToBarData(
        periodData: List<Stared>,
        periodType: String,
        startPeriod: LocalDate,
        endPeriod: LocalDate
    ): BarData {
        val values = arrayListOf<BarEntry>()
        var startDay = startPeriod

        var lastDay = when (periodType) {
            "week" -> startDay
            "month" -> startDay.with(DayOfWeek.SUNDAY)
            "year" -> startDay.withDayOfMonth(startDay.lengthOfMonth())
            else -> throw IllegalArgumentException("Illegal period type found")
        }
        var entryIndex = 0
        while (lastDay != endPeriod) {
            if (lastDay > endPeriod) {
                lastDay = endPeriod
            }
            val stared = periodData.filter { it.time in startDay..lastDay }.sortedBy { it.time }
            values.add(BarEntry(entryIndex.toFloat(), stared.sumOf { it.users.size }.toFloat(), stared))

            when (periodType) {
                "week" -> {
                    startDay = startDay.plusDays(1)
                    lastDay = startDay
                }

                "month" -> {
                    startDay = startDay.plusWeeks(1).with(DayOfWeek.MONDAY)
                    if (lastDay != endPeriod) lastDay = startDay.with(DayOfWeek.SUNDAY)
                }

                "year" -> {
                    startDay = startDay.plusMonths(1).withDayOfMonth(1)
                    if (lastDay != endPeriod) lastDay = startDay.withDayOfMonth(startDay.lengthOfMonth())
                }

                else -> throw IllegalArgumentException("Illegal period type found")
            }

            entryIndex++
        }

        val set = BarDataSet(values, "[$startPeriod]<->[$endPeriod]")
        return BarData(set)
    }
}