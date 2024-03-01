package com.example.gitapp.data.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DB = "migration-test"

@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {
    @JvmField
    @Rule
    val migrationHelper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), AppDatabase::class.java)

    @Test
    fun migrate1To2() {
        var db = migrationHelper.createDatabase(TEST_DB, 1)

        db.execSQL("INSERT INTO repo (repo_name, name_user, avatar_url, is_favorite) VALUES ('name repo', 'name user', 'avatar url', 0)")
        db.close()

        db = migrationHelper.runMigrationsAndValidate(TEST_DB, 2, true, AppDatabase.MIGRATION_1_2)

        val cursor = db.query("SELECT * FROM repo")
        TestCase.assertEquals(1, cursor.count)

        while (cursor.moveToNext()) {
            TestCase.assertEquals(0, cursor.getInt(cursor.getColumnIndex("is_private")))
        }
    }
}