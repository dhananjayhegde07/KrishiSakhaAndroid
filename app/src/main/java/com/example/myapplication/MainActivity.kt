package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.Screens.Login.LoadingScreen
import com.example.myapplication.Screens.Login.LoginSignupMaimScreen
import com.example.myapplication.Screens.Login.OtpValidation
import com.example.myapplication.Screens.Login.PreChecks
import com.example.myapplication.Screens.Login.home.CameraScreen
import com.example.myapplication.Screens.Login.home.ChatScreen
import com.example.myapplication.Screens.Login.home.CropRecommendScreen
import com.example.myapplication.Screens.Login.home.DrawerInit
import com.example.myapplication.Screens.Login.home.FertilizerScreen
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.SharedPreference
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context=applicationContext
        DataBaseObject.init(context)
        SharedPreference.init(this)
        setContent {
            App()
        }

    }
}


@Composable
fun App(){
    val navController= rememberNavController()
    GlobalStates.globalStates.initNavController(navController)
    NavHost(navController=navController , startDestination = "pre"){
        composable("pre"){
            PreChecks(navController)
        }
        composable("login"){
            LoginSignupMaimScreen(navController)
        }
        composable("otp/{id}"){
            back-> OtpValidation(navController,back.arguments?.getString("id"))
        }
        composable("home"){
            DrawerInit(navController)
        }
        composable("chat"){
            ChatScreen()
        }
        composable("camera"){
            CameraScreen()
        }
        composable("fertilizer"){
            FertilizerScreen()
        }
        composable("crop"){
            CropRecommendScreen()
        }
    }

    if (GlobalStates.globalStates.loadingScreen.value){
        LoadingScreen()
    }

    SnackbarHost(SnackBar.getSnackbarHostState())
}
