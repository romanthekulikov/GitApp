package com.example.gitapp.ui.diagram.utils

import com.example.gitapp.entity.Stared
import com.example.gitapp.ui.diagram.PeriodType
import com.github.mikephil.charting.data.BarData
import java.time.LocalDate

interface HistogramPeriodAdapter{
    fun periodToBarData(
        periodData: List<Stared>,
        periodType: PeriodType,
        startPeriod: LocalDate,
        endPeriod: LocalDate
    ): BarData
}