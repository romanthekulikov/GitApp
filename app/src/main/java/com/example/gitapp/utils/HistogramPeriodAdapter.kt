package com.example.gitapp.utils

import com.example.gitapp.data.api.models.ApiStarredData
import com.github.mikephil.charting.data.BarData

interface HistogramPeriodAdapter{
    fun periodToBarData(periodData: List<List<ApiStarredData>>, periodText: String): BarData
}