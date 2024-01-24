package com.example.gitapp.data.database

import androidx.room.Entity
import com.example.gitapp.entity.Owner
import com.example.gitapp.entity.Repo

@Entity
data class DbRepo(
    override val id: Int,
    override val name: String,
    override val isPrivate: Boolean,
    override val owner: Owner,
    override val stargazersCount: Int
) : Repo
