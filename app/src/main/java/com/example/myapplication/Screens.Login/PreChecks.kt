package com.example.myapplication.Screens.Login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.singleton.SharedPreference
import com.example.myapplication.singleton.SocketManager
import com.example.myapplication.singleton.userDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PreChecks(navController: NavController){
    LaunchedEffect(Unit) {
        val username =SharedPreference.getData("username")
        val jwt= SharedPreference.getData("jwt")
        if (username==null || jwt==null){
            navController.navigate("login"){
                popUpTo(navController.graph.startDestinationId){inclusive=true}
            }
            return@LaunchedEffect
        }
        userDetail.username=username.toString()
        userDetail.jwt=jwt.toString()
        userDetail.principal= withContext(Dispatchers.IO){
            DataBaseObject.getDao()?.getPrincipal(username.toString())
        }
        Log.d("TAG", "PreChecks: ${userDetail.jwt}")
        SocketManager.init()
        navController.navigate("home"){
            popUpTo(navController.graph.startDestinationId){inclusive=true}
        }
    }
}