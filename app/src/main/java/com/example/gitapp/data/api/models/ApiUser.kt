package com.example.gitapp.data.api.models

import android.os.Parcelable
import com.example.gitapp.entity.User
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class ApiUser @Inject constructor(
    @Json(name = "login")
    override val name: String,
    @Json(name = "avatar_url")
    override val iconUrl: String
) : User, Parcelable
