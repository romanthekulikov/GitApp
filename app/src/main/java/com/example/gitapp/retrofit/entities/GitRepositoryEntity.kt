package com.example.gitapp.retrofit.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class GitRepositoryEntity(
    @Json(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @Json(name = "name")
    val repositoryName: String,

    @Json(name = "private")
    val isPrivate: Boolean,

    @Json(name = "owner")
    val owner: Owner,

    @Json(name = "stargazers_count")
    val stargazersCount: Int
)

data class Owner(
    @Json(name = "login")
    val name: String,

    @Json(name = "avatar_url")
    val avatarUrl: String
)
