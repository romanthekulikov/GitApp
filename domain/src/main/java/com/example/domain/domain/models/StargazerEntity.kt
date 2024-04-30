package com.example.domain.domain.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.domain.domain.entity.Stargazer
import java.time.LocalDate

@Entity(
    tableName = "stargazer",
    foreignKeys = [ForeignKey(
        entity = RepoEntity::class,
        parentColumns = ["repo_name", "name_user"],
        childColumns = ["stared_repo", "owner_name"]
    )],
    primaryKeys = ["stared_repo", "owner_name", "name_user"]
)
data class StargazerEntity(
    @ColumnInfo(name = "time")
    override val time: LocalDate,
    @Embedded
    override val user: UserEntity,
    @ColumnInfo(name = "stared_repo")
    val repoName: String,
    @ColumnInfo(name = "owner_name")
    val ownerName: String
) : Stargazer
