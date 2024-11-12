package com.example.myapplication.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(entity = User::class)
    fun insert(user: User)

    @Query("select * from user")
    fun gett(): List<User>

}