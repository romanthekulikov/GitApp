package com.example.domain.domain.use_cases.diagram

import com.example.domain.domain.Repository
import java.time.LocalDate

class GetDateLoadedStargazerUseCase(private val repository: Repository) {
    fun executeLast(): LocalDate {
        return repository.getLastDateLoadedStargazer()
    }

    fun executeFirst(): LocalDate {
        return repository.getFirstLoadedStargazerDate()
    }
}