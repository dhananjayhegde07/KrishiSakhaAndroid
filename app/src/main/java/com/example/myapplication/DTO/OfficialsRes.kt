package com.example.myapplication.DTO

import com.example.myapplication.utils.Officials

data class SessionChatRes(
    val isAvailable: Boolean,
    val official: Officials? = null,
    val chatID: String?=null
)
