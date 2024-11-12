package com.example.myapplication.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase(){
    abstract fun userDao(): UserDao
}