package com.example.gitapp.data.api.entities

import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.Locale

data class GitStarredEntity(
    @Json(name = "starred_at")
    var starredTimeString: String,
    @Json(name = "user")
    val starredUser: StarredUser
) {
    fun getTime(): Long {
        return SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
            .parse(starredTimeString.substring(0, 9))?.time ?: System.currentTimeMillis()
    }
}

data class StarredUser(
    @Json(name = "login")
    val name: String,
    @Json(name = "avatar_url")
    val iconUrl: String
)
