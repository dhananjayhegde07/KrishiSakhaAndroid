package com.example.myapplication.Screens.Login.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.myapplication.DTO.DetectRes
import com.example.myapplication.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPage(showResults: MutableState<Boolean>,result: MutableState<DetectRes?>){
    val sheetState= rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope= rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showResults.value=false
            scope.launch{
                sheetState.hide()
            }
        },
        modifier = Modifier.fillMaxSize()
    ){
        if (result.value==null){
            Text("unable to Detect")
            return@ModalBottomSheet
        }
        Text(result.value?.detected.toString())
        Text(result.value?.disease.toString())
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingButton(navController: NavController){
    var menuExpanded = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000),
        label = "null"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (menuExpanded.value) Color(0x481C1C1C) else Color.Transparent)
            .padding(bottom = 60.dp, end = 30.dp)
        ,
        contentAlignment = Alignment.BottomEnd // Aligns the FAB to the bottom-end
    ) {
        // FloatingActionButton
        FloatingActionButton(
            onClick = {
                if(menuExpanded.value) return@FloatingActionButton
                menuExpanded.value=true
            },
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "logo"
            )

        }
        if (menuExpanded.value){
            Popup(
                alignment = Alignment.BottomEnd,
                offset = _root_ide_package_.androidx.compose.ui.unit.IntOffset(x = -15,y = -200),
                onDismissRequest = {menuExpanded.value=false},
            ) {
                    Box(
                        modifier = Modifier
                            .scale(scale.value)
                    ) {
                        Column {
                            ItemMenu(painterResource(R.drawable.file_send_svgrepo_com),  {
                                menuExpanded.value=false
                                navController.navigate("camera")
                            })
                            Spacer(Modifier.height(10.dp))
                            ItemMenu(painterResource(R.drawable.chat_svgrepo_com),{
                                menuExpanded.value=false
                                navController.navigate("chat")
                            })
                            Spacer(Modifier.height(10.dp))
                            ItemMenu(painterResource(R.drawable.fertilizer_svgrepo_com), {
                                menuExpanded.value=false
                                navController.navigate("fertilizer")
                            })
                            Spacer(Modifier.height(10.dp))
                            ItemMenu(painterResource(R.drawable.hand_holding_seedling_svgrepo_com), {
                                menuExpanded.value=false
                                navController.navigate("crop")
                            })
                        }
                    }
                }
        }
    }
}

@Composable
fun ItemMenu(icon: Painter,onNav:()->Unit){
    Box(contentAlignment = Alignment.Center, modifier = Modifier.height(50.dp).width(50.dp).clip(RoundedCornerShape(50)).background(
        Color.White).clickable{onNav()}){
        Icon(
            painter = icon, contentDescription = "home",
            modifier = Modifier.height(25.dp)
        )
    }
}