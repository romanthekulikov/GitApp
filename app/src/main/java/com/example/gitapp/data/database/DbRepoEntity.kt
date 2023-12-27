package com.example.gitapp.data.database

import androidx.room.Entity
import com.example.gitapp.data.entity.Owner
import com.example.gitapp.data.entity.RepoEntity

@Entity
data class DbRepoEntity(
    override val id: Int,
    override val repositoryName: String,
    override val isPrivate: Boolean,
    override val owner: Owner,
    override val stargazersCount: Int
) : RepoEntity
