package com.example.myapplication.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
class User {
    @PrimaryKey()
    var username:String=""
    var name:String?=null
    var email:String?=null
    var password:String?=null
    var phone:String?=null
    var pin:String?=null
}