package com.example.gitapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitapp.data.api.models.ApiStarredData

@Dao
interface StargazerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stargazers: List<ApiStarredData>)

    @Query("SELECT * FROM stargazer WHERE starred_repo = :repoName")
    suspend fun getStargazers(repoName: String): List<ApiStarredData>
}