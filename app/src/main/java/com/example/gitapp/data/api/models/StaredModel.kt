package com.example.gitapp.data.api.models

import com.example.gitapp.entity.Stared
import com.example.gitapp.entity.User
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class StaredModel(
    override val time: LocalDate,
    override val users: List<User>
) : Stared