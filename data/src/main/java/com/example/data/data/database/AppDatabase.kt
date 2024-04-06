package com.example.data.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.data.converters.LocalDateConverter
import com.example.data.data.database.dao.RepoDao
import com.example.data.data.database.dao.StargazerDao
import com.example.data.data.database.entity.RepoEntity
import com.example.data.data.database.entity.StargazerEntity

@Database(
    entities = [RepoEntity::class, StargazerEntity::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [ AutoMigration(from = 1, to = 2), AutoMigration(from = 2, to = 3) ]
)

@TypeConverters(value = [LocalDateConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun stargazerDao(): StargazerDao

    companion object {
        lateinit var db: AppDatabase

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE repo ADD COLUMN is_private INTEGER NOT NULL DEFAULT(0)")
            }
        }

        fun initDb(applicationContext: Context) {
            db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "git_db")
                //.addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}