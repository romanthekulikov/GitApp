package com.example.gitapp.data.api.models

import android.os.Parcelable
import com.example.gitapp.entity.User
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiUser(
    @Json(name = "login")
    override val name: String,
    @Json(name = "avatar_url")
    override val iconUrl: String
) : User, Parcelable
