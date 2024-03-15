package com.example.gitapp.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.example.gitapp.entity.Repo
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "repo",
    primaryKeys = ["repo_name", "name_user"]
)
@Parcelize
data class RepoEntity(
    @ColumnInfo(name = "repo_name")
    override val name: String,
    @Embedded
    override val owner: UserEntity,
    @ColumnInfo(name = "stargazer_count")
    override var stargazersCount: Int,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "is_private", defaultValue = "false")
    var isPrivate: Boolean = false,
    @ColumnInfo(name = "is_notified", defaultValue = "false")
    var isNotified: Boolean = false
) : Repo, Parcelable
