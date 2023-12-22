package com.example.gitapp.retrofit

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class GitRepositoryResponse(
    @Json(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @Json(name = "name")
    val repositoryName: String,

    @Json(name = "private")
    val isPrivate: Boolean,

    @Json(name = "owner")
    val owner: Owner
)

data class Owner(
    @Json(name = "login")
    val ownerName: String,

    @Json(name = "avatar_url")
    val avatarUrl: String
)
