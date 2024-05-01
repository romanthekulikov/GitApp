package com.example.domain.domain.use_cases.diagram

import com.example.domain.domain.HistogramPeriodAdapter
import com.example.domain.domain.entity.Stared
import com.github.mikephil.charting.data.BarData
import java.time.LocalDate

class GetBarDataUseCase(private val histogramPeriodAdapter: HistogramPeriodAdapter) {
    fun execute(loadedData: List<Stared>, diagramMode: String, startPeriod: LocalDate, endPeriod: LocalDate): BarData {
        return histogramPeriodAdapter.periodToBarData(loadedData, diagramMode, startPeriod, endPeriod)
    }
}