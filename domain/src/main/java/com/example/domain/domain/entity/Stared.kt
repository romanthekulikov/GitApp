package com.example.domain.domain.entity

import android.os.Parcelable
import java.time.LocalDate

interface Stared : Parcelable {
    val time: LocalDate
    val users: List<User>
}