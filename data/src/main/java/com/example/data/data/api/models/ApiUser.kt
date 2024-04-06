package com.example.data.data.api.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.example.domain.domain.entity.User
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiUser(
    @Json(name = "login")
    @ColumnInfo(name = "name_user")
    override var nameUser: String,

    @Json(name = "avatar_url")
    @ColumnInfo(name = "avatar_url")
    override var avatarUrl: String
) : User, Parcelable {
    constructor(user: User) : this(user.nameUser, user.avatarUrl) {
        this.nameUser = user.nameUser
        this.avatarUrl = user.avatarUrl
    }
}
