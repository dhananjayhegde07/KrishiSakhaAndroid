package com.example.myapplication.singleton

import com.example.myapplication.DataBase.User
import kotlin.concurrent.Volatile

object userDetail {

    @Volatile
    var username: String=""

    @Volatile
    var jwt: String =""

    @Volatile
    var principal: User?=null

}