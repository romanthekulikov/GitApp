package com.example.gitapp.utils.implementation

import com.example.gitapp.ui.diagram.models.Month
import com.example.gitapp.ui.diagram.models.Period
import com.example.gitapp.data.PeriodType
import com.example.gitapp.ui.diagram.models.Week
import com.example.gitapp.ui.diagram.models.Year
import com.example.gitapp.utils.HistogramPeriodAdapter
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import javax.inject.Inject

class HistogramPeriodAdapterImpl @Inject constructor() : HistogramPeriodAdapter {
    override fun periodToBarData(period: Period, periodType: PeriodType, periodText: String): BarData {
        return when (periodType) {
            PeriodType.WEEK -> weakToBarData(period as Week, periodText)
            PeriodType.MONTH -> monthToBarData(period as Month, periodText)
            PeriodType.YEAR -> yearToBarData(period as Year, periodText)
        }
    }

    private fun weakToBarData(week: Week, periodText: String): BarData {
        val values = mutableListOf<BarEntry>()
        for (day in week.weekDays.indices) {
            val dayNumberFloat = day.toFloat()
            val dayStargazerCountFloat = week.weekDays[day].size.toFloat()
            values.add(BarEntry(dayNumberFloat, dayStargazerCountFloat))
        }

        val set = BarDataSet(values, periodText)
        return BarData(set)
    }

    private fun monthToBarData(month: Month, periodText: String): BarData {
        val values = mutableListOf<BarEntry>()
        for (week in month.weeks.indices) {
            val numberWeekFloat = week.toFloat()
            val weekStargazerCount = month.weeks[week].getStargazerCount().toFloat()
            values.add(BarEntry(numberWeekFloat, weekStargazerCount))
        }

        val set = BarDataSet(values, periodText)
        return BarData(set)
    }

    private fun yearToBarData(year: Year, periodText: String): BarData {
        val values = mutableListOf<BarEntry>()
        for (month in year.months.indices) {
            val numberMonthFloat = month.toFloat()
            val monthStargazerCount = year.months[month].getStargazerCount().toFloat()
            values.add(BarEntry(numberMonthFloat, monthStargazerCount))
        }

        val set = BarDataSet(values, periodText)
        return BarData(set)
    }
}