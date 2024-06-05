package com.example.domain.domain.use_cases.diagram

import com.example.domain.domain.entity.Stared
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.time.DayOfWeek
import java.time.LocalDate

const val EXCEPTION_ILLEGAL_PERIOD = "Illegal period type found"
const val WEEK = "week"
const val MONTH = "month"
const val YEAR = "year"

class AdaptPeriodUseCase {

    fun periodToBarData(
        periodData: List<Stared>,
        periodType: String,
        startPeriod: LocalDate,
        endPeriod: LocalDate
    ): BarData {
        val values = arrayListOf<BarEntry>()
        var startDay = startPeriod

        var lastDay = when (periodType) {
            WEEK -> startDay
            MONTH -> startDay.with(DayOfWeek.SUNDAY)
            YEAR -> startDay.withDayOfMonth(startDay.lengthOfMonth())
            else -> throw IllegalArgumentException(EXCEPTION_ILLEGAL_PERIOD)
        }
        var entryIndex = 0
        var addLast = false
        while (lastDay != endPeriod) {
            if (lastDay > endPeriod) {
                lastDay = endPeriod
                addLast = true
            }

            values.add(getBarEntry(periodData, entryIndex, startDay, lastDay))

            when (periodType) {
                WEEK -> {
                    startDay = startDay.plusDays(1)
                    lastDay = startDay
                    if (lastDay == endPeriod) values.add(getBarEntry(periodData, entryIndex++, startDay, lastDay))
                }

                MONTH -> {
                    startDay = startDay.plusWeeks(1).with(DayOfWeek.MONDAY)
                    if (lastDay != endPeriod) lastDay = startDay.with(DayOfWeek.SUNDAY)
                }

                YEAR -> {
                    startDay = startDay.plusMonths(1).withDayOfMonth(1)
                    if (lastDay != endPeriod) lastDay = startDay.withDayOfMonth(startDay.lengthOfMonth())
                }

                else -> throw IllegalArgumentException(EXCEPTION_ILLEGAL_PERIOD)
            }

            entryIndex++
        }

        if (!addLast) {
            values.add(getBarEntry(periodData, entryIndex, startDay, lastDay))
        }

        val set = BarDataSet(values, "[$startPeriod]<->[$endPeriod]")
        return BarData(set)
    }

    private fun getBarEntry(periodData: List<Stared>, entryIndex: Int, startDay: LocalDate, lastDay: LocalDate): BarEntry {
        val stared = periodData.filter { it.time in startDay..lastDay }.sortedBy { it.time }
        return BarEntry(entryIndex.toFloat(), stared.sumOf { it.users.size }.toFloat(), stared)
    }
}