package com.example.gitapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.example.gitapp.data.converters.LocalDateConverter
import com.example.gitapp.data.database.dao.RepoDao
import com.example.gitapp.data.database.dao.StargazerDao
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.data.database.entity.StargazerEntity

@Database(entities = [RepoEntity::class, StargazerEntity::class], version = 2, exportSchema = true)
@TypeConverters(value = [LocalDateConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun stargazerDao(): StargazerDao

    companion object {
        lateinit var db: AppDatabase

        val MIGRATION_1_2 = Migration(1, 2) { db ->
            db.execSQL("ALTER TABLE repo ADD COLUMN is_private INTEGER NOT NULL DEFAULT(0)")
        }
        fun initDb(applicationContext: Context) {
            db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "git_db")
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}