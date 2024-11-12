package com.example.myapplication.Screens.Login

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.myapplication.singleton.SharedPreference
import com.example.myapplication.singleton.userDetail

@Composable
fun PreChecks(navController: NavController){
    val username =SharedPreference.getData("username")
    val jwt= SharedPreference.getData("jwt")
    if (username==null || jwt==null){
        navController.navigate("login"){
            popUpTo(navController.graph.startDestinationId){inclusive=true}
        }
        return
    }
    userDetail.username=username.toString()
    userDetail.jwt=jwt.toString()
    navController.navigate("home"){
        popUpTo(navController.graph.startDestinationId){inclusive=true}
    }
}