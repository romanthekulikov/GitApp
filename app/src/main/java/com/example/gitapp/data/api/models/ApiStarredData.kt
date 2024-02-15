package com.example.gitapp.data.api.models

import android.os.Parcelable
import com.example.gitapp.entity.StarredData
import com.squareup.moshi.Json
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import javax.inject.Inject

@Parcelize
data class ApiStarredData @Inject constructor(
    @Json(name = "starred_at")
    override var timeString: String,
    @Json(name = "user")
    override val user: ApiUser
): StarredData, Parcelable {
    init {
        timeString = timeString.substring(0, 10)
    }
    @IgnoredOnParcel
    val time: LocalDate = LocalDate.parse(timeString)
}