package com.example.myapplication.DataBase

import android.content.Context
import androidx.room.Room

object DataBaseObject {
    @Volatile
    var INSTANCE: DataBase? = null
    // Function to get the instance of the database
    fun init(context: Context) {
        INSTANCE=Room.databaseBuilder(
            context,
            DataBase::class.java,
            "app_database"
        ).build()
    }

    fun getDao(): UserDao? {
        return INSTANCE?.userDao()
    }

}