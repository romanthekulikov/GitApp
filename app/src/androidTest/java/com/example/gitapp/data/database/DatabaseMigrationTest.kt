package com.example.gitapp.data.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

private const val TEST_DB = "migration-test"

@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {
    @Suppress("DEPRECATION")
    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        com.example.data.data.database.AppDatabase::class.java.canonicalName!!,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        var db = helper.createDatabase(TEST_DB, 1)

        db.execSQL("INSERT INTO repo (repo_name, name_user, avatar_url, stargazer_count, is_favorite) VALUES ('name repo', 'name user', 'avatar url', 15, 0)")
        db.close()

        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, com.example.data.data.database.AppDatabase.MIGRATION_1_2)

        val cursor = db.query("SELECT * FROM repo")
        TestCase.assertEquals(1, cursor.count)

        while (cursor.moveToNext()) {
            TestCase.assertEquals(0, cursor.getInt(cursor.getColumnIndex("is_private")))
        }
    }
}