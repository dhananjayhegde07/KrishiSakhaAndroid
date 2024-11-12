package com.example.myapplication.DTO


data class LoginRes(
    var username: String?="",
    var jwt: String ?="",
    var msg: String?="",
    var status: String?="",
    var otpId: String?=""
)