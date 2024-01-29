package com.example.gitapp.data.api.models

import com.example.gitapp.entity.StarredData
import com.squareup.moshi.Json
import java.time.LocalDate
import javax.inject.Inject


data class ApiStarredData @Inject constructor(
    @Json(name = "starred_at")
    override var time: String,
    @Json(name = "user")
    override val user: ApiUser
): StarredData {
    init {
        time = time.substring(0, 10)
    }
    override fun getLocalDate(): LocalDate {
        return LocalDate.parse(time)
    }
}
