package com.example.myapplication.DTO

data class PestRecommend (
    val insect: String,
    val pest: List<String>,
    val confidence: Double? = null // Optional, based on FastAPI response
)