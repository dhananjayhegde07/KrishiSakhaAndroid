package com.example.myapplication.singleton

import kotlin.concurrent.Volatile

object userDetail {

    @Volatile
    var username: String=""

    @Volatile
    var jwt: String =""
}