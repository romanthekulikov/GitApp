package com.example.domain.domain

import com.example.domain.domain.entity.Stared
import com.github.mikephil.charting.data.BarData
import java.time.LocalDate

interface HistogramPeriodAdapter {
    fun periodToBarData(
        periodData: List<Stared>,
        periodType: String,
        startPeriod: LocalDate,
        endPeriod: LocalDate
    ): BarData
}