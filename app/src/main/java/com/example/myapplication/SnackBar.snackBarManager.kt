package com.example.myapplication

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState

object SnackBar {
    val s = SnackbarHostState()

    fun getSnackbarHostState(): SnackbarHostState{
        return s
    }

    suspend fun showSnack(message:String,duration: SnackbarDuration){
        s.showSnackbar(message=message,duration=duration, actionLabel = "ok")
    }
}