package com.example.myapplication.DTO

data class ResendOtpReq(
    var optId: String?,
)


data class ResendOtpRes(
    val otpId: String
)