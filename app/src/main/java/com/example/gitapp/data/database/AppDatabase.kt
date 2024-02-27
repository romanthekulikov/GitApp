package com.example.gitapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gitapp.data.converters.LocalDateConverter
import com.example.gitapp.data.database.dao.RepoDao
import com.example.gitapp.data.database.dao.StargazerDao
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.data.database.entity.StargazerEntity

@Database(entities = [RepoEntity::class, StargazerEntity::class], version = 1, exportSchema = true)
@TypeConverters(value = [LocalDateConverter::class])
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