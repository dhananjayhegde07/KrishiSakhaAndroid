package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.Screens.Login.LoadingScreen
import com.example.myapplication.Screens.Login.LoginSignupMaimScreen
import com.example.myapplication.Screens.Login.OTPVerificationScreen
import com.example.myapplication.Screens.Login.PreChecks
import com.example.myapplication.Screens.Login.ResetScreen
import com.example.myapplication.Screens.Login.home.CameraScreen
import com.example.myapplication.Screens.Login.home.CameraScreen_pestiside
import com.example.myapplication.Screens.Login.home.ChatScreen
import com.example.myapplication.Screens.Login.home.CropRecommendScreen
import com.example.myapplication.Screens.Login.home.DrawerInit
import com.example.myapplication.Screens.Login.home.FertilizerScreen
import com.example.myapplication.Screens.Login.home.RecentPage
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
    NavHost(
        navController=navController ,
        startDestination = "pre",
        enterTransition = {
            slideInVertically(
            animationSpec = tween(700),
            initialOffsetY = { it } // Start from bottom
        ) },
        exitTransition = {
            slideOutVertically(
                animationSpec = tween(700),
                targetOffsetY = { it } // End at the bottom
            )
        },
        popEnterTransition = { slideInHorizontally(tween(700)) { it } },
        popExitTransition = { slideOutHorizontally(tween(700)) { -it } }
    ){
        composable("pre"){
            PreChecks(navController)
        }
        composable("login"){
            LoginSignupMaimScreen(navController)
        }
        composable("otp/{id}"){
            back->
            OTPVerificationScreen(navController, back.arguments?.getString("id").toString())
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
        composable("reset"){
            ResetScreen()
        }
        composable("recent/{id}"){
            RecentPage(it.arguments?.getString("id"))
        }
        composable("pest"){
            CameraScreen_pestiside()
        }
    }

    if (GlobalStates.globalStates.loadingScreen.value){
        LoadingScreen()
    }

    SnackbarHost(SnackBar.getSnackbarHostState())
}
