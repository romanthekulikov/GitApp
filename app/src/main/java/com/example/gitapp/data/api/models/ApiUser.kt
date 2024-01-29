package com.example.gitapp.data.api.models

import com.example.gitapp.entity.User
import com.squareup.moshi.Json
import javax.inject.Inject

data class ApiUser @Inject constructor(
    @Json(name = "login")
    override val name: String,
    @Json(name = "avatar_url")
    override val iconUrl: String
) : User