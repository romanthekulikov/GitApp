package com.example.gitapp.data.api.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitapp.entity.Repo
import com.squareup.moshi.Json

@Entity
data class ApiRepo(
    @Json(name = "id")
    @PrimaryKey(autoGenerate = true)
    override val id: Int,

    @Json(name = "name")
    override val name: String,

    @Json(name = "private")
    override val isPrivate: Boolean,

    @Json(name = "owner")
    override val owner: ApiOwner,

    @Json(name = "stargazers_count")
    override val stargazersCount: Int
) : Repo
