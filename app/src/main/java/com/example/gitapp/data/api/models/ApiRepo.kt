package com.example.gitapp.data.api.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitapp.entity.Repo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "repo")
@JsonClass(generateAdapter = true)
data class ApiRepo(

    @Json(name = "name")
    @PrimaryKey
    override var name: String,

    @Json(name = "owner")
    @Embedded
    override val owner: ApiUser,

    @Json(name = "stargazers_count")
    @ColumnInfo(name = "stargazers_count")
    override val stargazersCount: Int,

    @Json(ignore = true)
    var isFavorite: Boolean = false
) : Repo, Parcelable
