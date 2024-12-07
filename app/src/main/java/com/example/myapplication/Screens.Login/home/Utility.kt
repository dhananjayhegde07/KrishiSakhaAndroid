package com.example.myapplication.Screens.Login.home

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.myapplication.DTO.DetectRes
import com.example.myapplication.DTO.DiseasesModel
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.DataBase.DiseaseSave
import com.example.myapplication.R
import com.example.myapplication.singleton.userDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPage(showResults: MutableState<Boolean>,result: MutableState<DetectRes?>){
    val sheetState= rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope= rememberCoroutineScope()
    val translate = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (result.value?.detected==true){
            val obj = DiseaseSave().apply {
                this.username = userDetail.username
                this.timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.format(Date())
                this.url = ""
                this.result = result.value?.disease
            }
            withContext(Dispatchers.IO){
                Log.d("TAG", "ResultsPage: Saving...")
                DataBaseObject.INSTANCE?.diseaseDao()?.save(obj)
            }
        }
    }
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
        when{
            result.value==null->{
                Unable(listOf(
                    "Check Internet Connection",
                    "Server May be offline",
                    "Retry again"
                ))
            }
            result.value?.detected == false -> {
                Column (
                    Modifier.fillMaxWidth()
                ){
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Text("Failed...")
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("Cannot Find Diseases")
                    Spacer(Modifier.height(20.dp))
                    Text("Image may be incorrect")
                    Spacer(Modifier.height(20.dp))
                    Text("Object may be incorrect")
                }
            }
            else -> {
                ShowRes(result.value?.disease)
            }
        }

    }
}

@Composable
fun ShowRes(result: DiseasesModel?) {
    val translate = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(state = rememberScrollState(), enabled = true)
    ) {
        Row(
            modifier = Modifier.height(60.dp).fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color(0x884C8325)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Found It...!!")
            Icon(painter = painterResource(R.drawable.translate_svgrepo_com), contentDescription = ""
                , modifier = Modifier.height(30.dp).clickable{
                    translate.value= ! translate.value
                }
            )
        }
        Spacer(Modifier.height(20.dp))
        Text("Name", fontSize = 18.sp, color = Color(0xBA313131))
        Spacer(
            Modifier
                .fillMaxWidth(.7f)
                .height(1.dp)
                .border(color = Color.Black, width = 1.dp))
        Row(
            modifier = Modifier
                .heightIn(min = 50.dp)
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                if(translate.value)
                    result?.kannadaName?:""
                else
                    result?.className?:""
            )
        }
        Spacer(Modifier.height(10.dp))
        Text("Description", fontSize = 18.sp, color = Color(0xBA313131))
        Spacer(
            Modifier
                .fillMaxWidth(.7f)
                .height(1.dp)
                .border(color = Color.Black, width = 1.dp))
        Row(
            modifier = Modifier
                .heightIn(min = 50.dp)
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                if(translate.value)
                    result?.kannadaDescription?:""
                else
                    result?.description?:""
            )
        }
        Text("Cause", fontSize = 18.sp, color = Color(0xBA313131))
        Spacer(
            Modifier
                .fillMaxWidth(.7f)
                .height(1.dp)
                .border(color = Color.Black, width = 1.dp))
        Row(
            modifier = Modifier
                .heightIn(min = 50.dp)
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                if(translate.value)
                    result?.kannadaCause?:""
                else
                    result?.cause?:""
            )
        }
        Text("Recommended Actions", fontSize = 18.sp, color = Color(0xBA313131))
        Spacer(
            Modifier
                .fillMaxWidth(.7f)
                .height(1.dp)
                .border(color = Color.Black, width = 1.dp))
        Column(
            modifier = Modifier.padding( start = 20.dp, top = 20.dp),
        ){
            result?.recommendedActions?.forEach {
                Text(
                    if(translate.value)
                        it.kannadaAction
                    else
                        it.action
                )
            }
        }
    }
}

@Composable
fun Unable(causes: List<String>){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the icon
        Icon(
            painter = painterResource(R.drawable.error_inspect_ios11_svgrepo_com),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        // Main error message
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Subtext explaining possible causes
        Text(
            text = "Possible reasons:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Display the list of causes
        causes.forEach { cause ->
            Text(
                text = "- $cause",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
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
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .height(50.dp)
        .width(50.dp)
        .clip(RoundedCornerShape(50))
        .background(
            Color.White
        )
        .clickable { onNav() }){
        Icon(
            painter = icon, contentDescription = "home",
            modifier = Modifier.height(25.dp)
        )
    }
}