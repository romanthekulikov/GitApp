package com.example.gitapp.data.api.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.gitapp.entity.StarredData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import javax.inject.Inject

@Parcelize
@Entity(tableName = "stargazer")
@JsonClass(generateAdapter = true)
data class ApiStarredData @Inject constructor(
    @Json(ignore = true)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @Json(name = "starred_at")
    @ColumnInfo(name = "time")
    override var timeString: String,

    @Json(name = "user")
    @Embedded
    override val user: ApiUser,

    @Json(ignore = true)
    @ColumnInfo(name = "starred_repo")
    var starredRepo: String = ""
) : StarredData, Parcelable {
    init {
        timeString = timeString.substring(0, 10)
    }

    @IgnoredOnParcel
    @Ignore
    val time: LocalDate = LocalDate.parse(timeString)
}