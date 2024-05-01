package com.example.domain.domain.use_cases.diagram

import com.example.domain.domain.PeriodHelper
import com.example.domain.domain.entity.Stared

class GetPeriodStringUseCase(private val periodHelper: PeriodHelper) {
    fun execute(periodData: List<Stared>, diagramMode: String): String {
        return periodHelper.getPeriodString(periodData, diagramMode)
    }
}