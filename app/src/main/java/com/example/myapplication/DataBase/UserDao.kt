package com.example.myapplication.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun insertUser(user : User): Unit

    @Query("select * from user")
    fun getUsers(): List<User>

    @Query("SELECT * FROM user  WHERE username = :username")
    fun getPrincipal(username: String): User?

    @Query("update user set name = :name, phone = :phone,email=:email,pin=:pin where username = :username")
    fun updateUser(name: String, phone: String, email: String, pin: String,username: String)

}