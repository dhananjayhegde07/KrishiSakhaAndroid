package com.example.myapplication.DataBase

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DataBaseObject {
    @Volatile
    var INSTANCE: DataBase? = null
    // Function to get the instance of the database
    fun init(context: Context) {
        INSTANCE=Room.databaseBuilder(
            context,
            DataBase::class.java,
            "app_database2"
        ).addMigrations(getMigration1_2(),getMigartion2_3())
            .build()

    }

    fun getDao(): UserDao? {
        return INSTANCE?.userDao()
    }

    fun destroy(){
        INSTANCE=null;
    }

    fun getMigration1_2(): Migration {
        return object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // SQL to create the new table
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS diseaseResult (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                username TEXT NOT NULL,
                timestamp TEXT NOT NULL,
                url TEXT NOT NULL,
                result TEXT
            )
        """)
            }
        }
    }


    fun getMigartion2_3(): Migration{
        return object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create FertilizerSave table
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS FertilizerSave (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                username TEXT NOT NULL,
                timestamp TEXT NOT NULL,
                input TEXT,
                result TEXT
            )
            """
                )

                // Create RecommendationSave table
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS RecommendationSave (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                username TEXT NOT NULL,
                timestamp TEXT NOT NULL,
                input TEXT,
                result TEXT
            )
            """
                )
            }
        }
    }
}