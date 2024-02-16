package com.example.gitapp.data.api.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.example.gitapp.entity.User
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiUser(
    @Json(name = "login")
    @ColumnInfo(name = "name_user")
    override var nameUser: String,

    @Json(name = "avatar_url")
    @ColumnInfo(name = "avatar_url")
    override val avatarUrl: String
) : User, Parcelable
