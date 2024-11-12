package com.example.myapplication.Screens.Login.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R


@Composable
fun Home(navController: NavController,nav:androidx.compose.runtime.MutableState<String>){
    Column(horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center
        , modifier = Modifier
            .fillMaxHeight()
            .clickable(
                enabled = true,
                onClick = {
                    navController.navigate("home")
                    nav.value="home"
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Icon(painter = painterResource(R.drawable.home_angle_svgrepo_com), contentDescription = "home",
            modifier = Modifier.scale(.5f))
    }
}


@Composable
fun Capture(navController: NavController, nav:androidx.compose.runtime.MutableState<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center
        , modifier = Modifier
            .fillMaxHeight()

            .clickable(
                enabled = true,
                onClick = {
                    navController.navigate("camera")
                    nav.value="camera"
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Icon(painter = painterResource(R.drawable.file_send_svgrepo_com), contentDescription = "home",
            modifier = Modifier.scale(.5f))

    }
}

@Composable
fun Fertilzer(navController: NavController, nav:androidx.compose.runtime.MutableState<String>){
    Column(horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center
        , modifier = Modifier
            .fillMaxHeight()

            .clickable(
                enabled = true,
                onClick = {
//                        navController.navigate("home")
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Icon(painter = painterResource(R.drawable.fertilizer_svgrepo_com), contentDescription = "home",
            tint = Color.Green,
            modifier = Modifier.scale(.5f)

        )
    }

//
}



@Composable
fun Chat(navController: NavController, nav:androidx.compose.runtime.MutableState<String>){
    Column(horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center
        , modifier = Modifier
            .fillMaxHeight()

            .clickable(
                enabled = true,
                onClick = {
                        navController.navigate("chat")
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Icon(painter = painterResource(R.drawable.chat_svgrepo_com), contentDescription = "home",
            modifier = Modifier.scale(.5f))

    }
}

@Composable
fun Dev(navController: NavController, nav:androidx.compose.runtime.MutableState<String>){
    Column(horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center
        , modifier = Modifier
            .fillMaxHeight()
            .clickable(
                enabled = true,
                onClick = {
//                        navController.navigate("home")
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )

    ) {
        Icon(
            painter = painterResource(R.drawable.developer_mode_svgrepo_com), contentDescription = "home",
            modifier = Modifier.scale(1.2f)
        )
        Box(
            modifier = Modifier
                .height(1.dp)            // Thickness of the line
                .background(color = androidx.compose.ui.graphics.Color.Gray)   // Color of the line
        )

    }
}
