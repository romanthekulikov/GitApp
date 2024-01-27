package com.example.gitapp.data.api.models

import com.example.gitapp.entity.StarredData
import com.squareup.moshi.Json
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject


data class ApiStarredData @Inject constructor(
    @Json(name = "starred_at")
    override var time: String,
    @Json(name = "user")
    override val user: ApiUser
): StarredData {

    override fun getLocalDate(): LocalDate {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale("ru"))
        return LocalDate.parse(time.substring(0, 10), format)
    }
}
