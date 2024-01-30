package com.example.gitapp.data.api.models

import android.os.Parcelable
import com.example.gitapp.entity.StarredData
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.time.LocalDate
import javax.inject.Inject

@Parcelize
data class ApiStarredData(
    @Json(name = "starred_at")
    override var time: String,
    @Json(name = "user")
    override val user: ApiUser
): StarredData, Serializable, Parcelable {
    init {
        time = time.substring(0, 10)
    }
    override fun getLocalDate(): LocalDate {
        return LocalDate.parse(time)
    }
}
