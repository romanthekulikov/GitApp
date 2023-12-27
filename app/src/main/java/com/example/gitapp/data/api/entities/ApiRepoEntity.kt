package com.example.gitapp.data.api.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitapp.data.entity.Owner
import com.example.gitapp.data.entity.RepoEntity
import com.squareup.moshi.Json

@Entity
data class GitRepositoryEntity(
    @Json(name = "id")
    @PrimaryKey(autoGenerate = true)
    override val id: Int,

    @Json(name = "name")
    override val repositoryName: String,

    @Json(name = "private")
    override val isPrivate: Boolean,

    @Json(name = "owner")
    override val owner: ApiOwner,

    @Json(name = "stargazers_count")
    override val stargazersCount: Int
) : RepoEntity

data class ApiOwner(
    @Json(name = "login")
    override val name: String,

    @Json(name = "avatar_url")
    override val avatarUrl: String
) : Owner
