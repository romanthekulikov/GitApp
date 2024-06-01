package com.example.data.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.domain.models.RepoEntity

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRepos(repos: List<RepoEntity>)

    @Query("SELECT * FROM repo WHERE LOWER(name_user) = LOWER(:ownerName)")
    suspend fun getOwnerRepoList(ownerName: String): List<RepoEntity>

    @Query("UPDATE repo SET is_favorite = :isFavorite WHERE repo_name = :repoName AND name_user = :ownerName")
    suspend fun updateRepoFavorite(ownerName: String, repoName: String, isFavorite: Boolean)

    @Query("UPDATE repo SET stargazer_count = :stargazersCount WHERE repo_name = :repoName AND name_user = :ownerName")
    suspend fun updateRepoStargazersCount(stargazersCount: Int, ownerName: String, repoName: String)

    @Query("SELECT * FROM repo WHERE repo_name = :repoName AND name_user = :ownerName")
    suspend fun getRepo(ownerName: String, repoName: String): RepoEntity?

    @Query("SELECT is_favorite FROM repo WHERE repo_name = :repoName AND LOWER(name_user) = LOWER(:ownerName)")
    suspend fun isFavoriteRepo(ownerName: String, repoName: String): Boolean

    @Query("SELECT * FROM repo WHERE is_favorite = 1")
    suspend fun getFavoriteRepoList(): List<RepoEntity>
}