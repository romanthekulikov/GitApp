package com.example.domain.domain.use_cases.diagram

import com.example.domain.domain.Repository

class ClearMemorySavedStargazersUseCase(private val repository: Repository) {
    fun execute() {
        repository.clearMemorySavedStargazers()
    }
}