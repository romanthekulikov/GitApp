package com.example.gitapp.data.api.entities

import com.example.gitapp.entity.Owner
import com.squareup.moshi.Json

data class ApiOwner(
    @Json(name = "login")
    override val name: String,

    @Json(name = "avatar_url")
    override val avatarUrl: String
) : Owner
