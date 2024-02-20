package com.example.gitapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitapp.data.database.entity.StargazerEntity

@Dao
interface StargazerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stargazers: List<StargazerEntity>)

    @Query("SELECT * FROM stargazer WHERE stared_repo = :repoName AND owner_name = :ownerName")
    suspend fun getStargazers(repoName: String, ownerName: String): List<StargazerEntity>
}