package com.example.myapplication.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
class User {
    @PrimaryKey(autoGenerate = true)
    var id: Int?=null
}