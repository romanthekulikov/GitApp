package com.example.data.data.database.entity

import androidx.room.ColumnInfo
import com.example.domain.domain.entity.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    @ColumnInfo(name = "name_user")
    override val nameUser: String,
    @ColumnInfo(name = "avatar_url")
    override val avatarUrl: String
) : User