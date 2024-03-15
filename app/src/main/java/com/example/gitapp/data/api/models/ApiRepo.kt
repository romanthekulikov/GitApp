package com.example.gitapp.data.api.models

import android.os.Parcelable
import androidx.room.Entity
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.data.database.entity.UserEntity
import com.example.gitapp.entity.Repo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "repo")
@JsonClass(generateAdapter = true)
data class ApiRepo(

    @Json(name = "name")
    override var name: String,

    @Json(name = "owner")
    override val owner: ApiUser,

    @Json(name = "stargazers_count")
    override var stargazersCount: Int,
) : Repo, Parcelable {
    fun toRepoEntity(isFavorite: Boolean): RepoEntity {
        return RepoEntity(
            name = name,
            owner = UserEntity(nameUser = owner.nameUser, avatarUrl = owner.avatarUrl),
            stargazersCount = stargazersCount,
            isFavorite = isFavorite
        )
    }
}
