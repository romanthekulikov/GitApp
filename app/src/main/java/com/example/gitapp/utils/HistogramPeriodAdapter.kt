package com.example.gitapp.utils

import com.example.gitapp.ui.diagram.models.Period
import com.example.gitapp.ui.diagram.PeriodType
import com.github.mikephil.charting.data.BarData

interface HistogramPeriodAdapter {
    fun periodToBarData(period: Period, periodType: PeriodType, periodText: String): BarData
}