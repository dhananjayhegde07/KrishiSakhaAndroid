package com.example.myapplication.Screens.Login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun LoginSignupMaimScreen(navController: NavController){
    var mode = rememberSaveable{ mutableStateOf(true) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment =Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth(.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (mode.value){
                AnimatedVisibility(
                    visible = true,
                    enter = expandIn(animationSpec = tween(durationMillis = 500)),

                ) {Login(navController) }
                Text(
                    text = "Don't have an account? Register",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable(enabled = true, onClick = {
                            mode.value=!mode.value;
                        },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                            )
                        .padding(top = 10.dp)
                )
            }
            else{
                Signup(navController)
                Text(
                    text = "Already have an account? Login",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable(enabled = true, onClick = {
                            mode.value=!mode.value;
                        } ,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        .padding(top = 10.dp)
                )
            }

        }
    }
}