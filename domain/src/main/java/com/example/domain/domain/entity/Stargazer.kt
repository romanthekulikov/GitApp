package com.example.domain.domain.entity

import java.time.LocalDate

interface Stargazer {
    val time: LocalDate
    val user: User
}