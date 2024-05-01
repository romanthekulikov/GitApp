package com.example.domain.domain.models

import com.example.domain.domain.entity.Stargazer
import com.example.domain.domain.entity.User
import java.time.LocalDate

data class StargazerModel(
    override val time: LocalDate,
    override val user: User
) : Stargazer
