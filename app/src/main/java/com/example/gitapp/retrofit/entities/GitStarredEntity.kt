package com.example.gitapp.retrofit.entities

import com.squareup.moshi.Json

data class GitStarredEntity(
    @Json(name = "starred_at")
    var starredTimeString: String,
    var starredTime: Long,
    @Json(name = "user")
    val starredUser: StarredUser
)

data class StarredUser(
    @Json(name = "login")
    val name: String,
    @Json(name = "avatar_url")
    val iconUrl: String
)
