package com.example.gitapp.entity

import java.time.LocalDate

interface Stargazer {
    val time: LocalDate
    val user: User
}