package com.example.gitapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitapp.data.api.models.ApiRepo

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(repos: List<ApiRepo>)

    @Query("SELECT * FROM repo WHERE LOWER(name_user) = LOWER(:ownerName)")
    suspend fun getOwnerRepos(ownerName: String): List<ApiRepo>

    @Query("UPDATE repo SET isFavorite = :isFavorite WHERE name = :repoName AND name_user = :ownerName")
    suspend fun updateFavoriteRepo(ownerName: String, repoName: String, isFavorite: Boolean)

    @Query("SELECT isFavorite FROM repo WHERE LOWER(name) = LOWER(:repoName) AND name_user = LOWER(:ownerName)")
    suspend fun isFavoriteRepo(ownerName: String, repoName: String): Boolean
}