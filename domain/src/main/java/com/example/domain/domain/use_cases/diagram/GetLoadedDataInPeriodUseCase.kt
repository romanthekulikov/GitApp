package com.example.domain.domain.use_cases.diagram

import com.example.domain.domain.Repository
import com.example.domain.domain.entity.Stared
import java.time.LocalDate

class GetLoadedDataInPeriodUseCase(private val repository: Repository) {
    fun execute(startPeriod: LocalDate, endPeriod: LocalDate): List<Stared> {
        return repository.getLoadedDataInPeriod(startPeriod, endPeriod)
    }
}