package com.example.gitapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.data.database.dao.RepoDao
import com.example.gitapp.data.database.dao.StargazerDao

@Database(entities = [ApiStarredData::class, ApiRepo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun stargazerDao(): StargazerDao


    companion object {
        lateinit var db: AppDatabase
        fun initDb(applicationContext: Context) {
            db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "git_db").build()
        }
    }
}