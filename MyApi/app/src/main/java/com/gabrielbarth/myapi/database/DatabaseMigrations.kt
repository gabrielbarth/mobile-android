package com.gabrielbarth.myapi.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    val MIGRATION_1_TO_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // create new table with new data structure
            database.execSQL(
                """
            CREATE TABLE new_location_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                latitude REAL NOT NULL,
                longitude REAL NOT NULL,
                createdAt INTEGER NOT NULL DEFAULT (strftime('%s','now')) 
            )
        """.trimIndent()
            )
            // copy old table data to the new table
            database.execSQL(
                """
            INSERT INTO new_location_table (id, latitude, longitude)
            SELECT id, latitude, longitude FROM user_location_table
        """.trimIndent()
            )
            // drop old table
            database.execSQL("DROP TABLE user_location_table")
            // rename the new table using the original table name
            database.execSQL("ALTER TABLE new_location_table RENAME TO user_location_table")
        }
    }
}