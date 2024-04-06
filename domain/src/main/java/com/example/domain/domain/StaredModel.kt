package com.example.domain.domain

import com.example.domain.domain.entity.Stared
import com.example.domain.domain.entity.User
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
internal data class StaredModel(
    override val time: LocalDate,
    override val users: List<User>
) : Stared