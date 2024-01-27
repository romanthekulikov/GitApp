package com.example.gitapp.entity

import com.example.gitapp.data.api.models.ApiUser
import java.time.LocalDate

interface StarredData {
    var time: String
    val user: ApiUser
    fun getLocalDate(): LocalDate
}