package com.example.domain.domain.models

import com.example.domain.domain.entity.Stared
import com.example.domain.domain.entity.User
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class StaredModel(
    override val time: LocalDate,
    override val users: List<User>
) : Stared