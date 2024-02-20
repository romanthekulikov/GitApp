package com.example.gitapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitapp.data.database.entity.RepoEntity

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(repos: List<RepoEntity>)

    @Query("SELECT * FROM repo WHERE LOWER(name_user) = LOWER(:ownerName)")
    suspend fun getOwnerRepos(ownerName: String): List<RepoEntity>

    @Query("UPDATE repo SET is_favorite = :isFavorite WHERE repo_name = :repoName AND name_user = :ownerName")
    suspend fun updateFavoriteRepo(ownerName: String, repoName: String, isFavorite: Boolean)

    @Query("SELECT is_favorite FROM repo WHERE repo_name = :repoName AND LOWER(name_user) = LOWER(:ownerName)")
    suspend fun isFavoriteRepo(ownerName: String, repoName: String): Boolean
}