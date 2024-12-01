package com.example.myapplication.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.myapplication.DTO.Messege
import com.example.myapplication.utils.WeatherResponse

class GlobalState: ViewModel(){
    var loadingScreen = mutableStateOf(false)
    var navController : NavHostController? = null
    var navVisible = mutableStateOf(true)
    var chatList= mutableStateListOf<Messege>()
    val weatherData= mutableStateOf<WeatherResponse?>(null)


    @Synchronized
    fun initNavController(navHostController: NavHostController){
        this.navController=navHostController
    }
    fun destroyNavController(){
        this.navController=null
    }
    fun loading(){
        this.loadingScreen.value=true
    }

    fun notLoading(){
        this.loadingScreen.value=false
    }

}