package com.example.gitapp.utils.implementation

import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.utils.HistogramPeriodAdapter
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import javax.inject.Inject

class HistogramPeriodAdapterImpl @Inject constructor() : HistogramPeriodAdapter {
    override fun periodToBarData(data: List<List<ApiStarredData>>, periodText: String): BarData {
        val values = arrayListOf<BarEntry>()
        var i = 0f
        for (stargazersList in data) {
            values.add(BarEntry(i, stargazersList.size.toFloat()))
            i++
        }
        val set = BarDataSet(values, periodText)
        return BarData(set)
    }
}