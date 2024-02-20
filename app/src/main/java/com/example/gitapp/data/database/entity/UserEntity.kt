package com.example.gitapp.data.database.entity

import androidx.room.ColumnInfo
import com.example.gitapp.entity.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    @ColumnInfo(name = "name_user")
    override val nameUser: String,
    @ColumnInfo(name = "avatar_url")
    override val avatarUrl: String
) : User