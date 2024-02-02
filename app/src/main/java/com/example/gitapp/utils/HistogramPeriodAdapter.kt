package com.example.gitapp.utils

import com.example.gitapp.data.PeriodType
import com.example.gitapp.ui.diagram.models.Period
import com.github.mikephil.charting.data.BarData

interface HistogramPeriodAdapter{
    fun periodToBarData(period: Period, periodType: PeriodType, periodText: String): BarData
}