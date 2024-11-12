package com.example.myapplication.Screens.Login.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.R
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.userDetail


@Composable
fun HomepageMainScreen(navController: NavController){
    Log.d("TAG", "HomepageMainScreen: ${userDetail.jwt}")

    LaunchedEffect(Unit) {
        try {
            Retrofit.api.getUser()
            val user=DataBaseObject.getDao()?.gett()
            Log.d("TAG", "HomepageMainScreen: ${user}")
        }
        catch (e: Exception){

        }
    }
    val homeNavController = rememberNavController();
    Column(
        Modifier.fillMaxSize()
    ) {
        Box(
            Modifier.weight(.1f).fillMaxHeight()
        ){
            TopView()
        }
        NavHost(navController=homeNavController, startDestination = "home",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight().weight(.82f)
                .background(
                    color = Color.White
                )
        ){
            composable("home"){
                HomeScreen(homeNavController)
            }
            composable("camera"){
                CameraScreen()
            }
            composable("chat"){
                ChatScreen()
            }
        }
        Box(
            Modifier.weight(.08f)
        ){
            bottomNav(homeNavController)
        }
    }

}

@Composable
fun bottomNav(navController: NavController){
    var nav=remember{ mutableStateOf("home") }
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .clip(RoundedCornerShape(10.dp)) // Clip applies the rounded corners
        .background(Color(0xFFA9A8A8))
    )
    Box(
        Modifier.fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(color = Color(0xFFD5D2D2))
            ,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ){
            Home(navController = navController,nav=nav)
            Capture(navController,nav)
            Chat(navController,nav)
            Fertilzer(navController,nav)
            Dev(navController,nav)
        }

    }

}


@Composable
fun HomeScreen(x0: NavController) {
    Text("Home")
    Icon(painter = painterResource(R.drawable.chat_svgrepo_com), contentDescription = "")
}