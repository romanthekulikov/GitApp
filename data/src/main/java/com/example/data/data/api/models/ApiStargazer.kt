package com.example.data.data.api.models

import android.os.Parcelable
import com.example.domain.domain.entity.Stargazer
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import javax.inject.Inject

@Parcelize
data class ApiStargazer @Inject constructor(
    @Json(name = "starred_at")
    override var time: LocalDate,
    @Json(name = "user")
    override val user: ApiUser
): Stargazer, Parcelable