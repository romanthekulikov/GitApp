package com.example.gitapp.database

import androidx.room.Entity

@Entity
data class RepositoryEntity(
    val ownerName: String,
    val repositoryName: String
)
