package com.example.gitapp.data.api.models

import com.example.gitapp.entity.Owner
import com.squareup.moshi.Json
import javax.inject.Inject

data class ApiOwner(
    @Json(name = "login")
    override val name: String,

    @Json(name = "avatar_url")
    override val avatarUrl: String
) : Owner
