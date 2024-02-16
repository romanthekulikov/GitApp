package com.example.gitapp.utils

import com.example.gitapp.ui.diagram.DiagramMode
import com.example.gitapp.data.api.models.ApiStarredData
import java.time.LocalDate

interface PeriodHelper {
    fun getPeriodString(partData: List<ApiStarredData>, diagramMode: DiagramMode): String
    fun getDataInPeriod(
        startPeriod: LocalDate,
        endPeriod: LocalDate,
        periodType: DiagramMode,
        stargazersItemsList: List<ApiStarredData>
    ): List<List<ApiStarredData>>
}