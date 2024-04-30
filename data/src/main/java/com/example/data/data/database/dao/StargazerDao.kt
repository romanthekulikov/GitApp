package com.example.data.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.domain.models.StargazerEntity

@Dao
interface StargazerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(stargazers: List<StargazerEntity>)

    @Query("SELECT * FROM stargazer WHERE stared_repo = :repoName AND LOWER(owner_name) = LOWER(:ownerName)")
    suspend fun getStargazers(repoName: String, ownerName: String): List<StargazerEntity>
}