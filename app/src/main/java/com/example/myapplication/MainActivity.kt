package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.Screens.Login.LoginSignupMaimScreen
import com.example.myapplication.Screens.Login.OtpValidation
import com.example.myapplication.Screens.Login.PreChecks
import com.example.myapplication.Screens.Login.home.HomepageMainScreen
import com.example.myapplication.singleton.SharedPreference
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBaseObject.init(applicationContext)
        SharedPreference.init(this)
        setContent {
            app()
        }

    }
}


@Composable
fun app(){
    val navController= rememberNavController()
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
            HomepageMainScreen(navController)
        }
    }
    SnackbarHost(SnackBar.getSnackbarHostState())
}
