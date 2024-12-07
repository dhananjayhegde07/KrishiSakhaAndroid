package com.example.myapplication.DTO

data class ResetInitReq(
    var username: String
)

data class ResetInitRes(
    var username: String,
    var otpID : String
)


data class ResetReq(
    val username: String,
    val password: String,
    val otpID: String,
    val otp: String
)