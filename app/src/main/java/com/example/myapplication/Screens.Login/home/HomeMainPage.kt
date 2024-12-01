package com.example.myapplication.Screens.Login.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.Screens.Login.components.GetSoilDetails
import com.example.myapplication.Screens.Login.components.Wheather
import com.example.myapplication.singleton.userDetail
import kotlinx.coroutines.launch

@Composable
fun DrawerInit(navController: NavController){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState =drawerState,
        drawerContent = {CustomDrawerContent({
            scope.launch{
                drawerState.close()
            }
        },
            {
                navController.navigate("login"){
                    popUpTo(navController.graph.startDestinationId){inclusive=true}
                }
            }
        ) },

    ) {
        HomepageMainScreen(navController,drawerState)
    }
}

@Composable
fun HomepageMainScreen(navController: NavController,drawerState: DrawerState){
    Log.d("TAG", "HomepageMainScreen: ${userDetail.jwt}")
    LaunchedEffect(Unit) {

    }
    Column(
       modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(.08f)
        ){
            TopView(drawerState)
        }
        Box(
            modifier = Modifier.weight(.92f)
        ){
            Content(navController)
        }

    }

}



@Composable
fun Content(navController: NavController){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(10.dp).verticalScroll(enabled = true, state = rememberScrollState())
    ){

        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                .height(200.dp).background(Color(0xAE968080)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Wheather()
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                .height(200.dp).background(Color(0xAE968080)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            GetSoilDetails()
        }
        Spacer(Modifier.height(20.dp))
        RecentDetections()
        Spacer(Modifier.height(10.dp))
        Text("What we Offer")
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(R.drawable.point_svgrepo_com), contentDescription = "",
                    modifier = Modifier.height(10.dp)
                )
                Text("Get detected")
            }
            Text("")
            Text("")
            Text("")
            Text("")
        }
    }
    FloatingButton(navController)
}

@Composable
fun GetService(painter: Painter,text:String,points: List<String>,onNav:()->Unit){
    Row(Modifier.fillMaxWidth().height(90.dp).clip(RoundedCornerShape(10.dp)).border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(.2f),
            contentAlignment = Alignment.Center
        ){
            Icon(painter =painter, contentDescription = null,
                modifier = Modifier.height(30.dp)
            )
        }
        Column(
            modifier = Modifier.weight(.6f)
        ) {
            points.forEach {
                Text(
                    text=it,
                    modifier = Modifier.height(30.dp).fillMaxWidth().padding(5.dp),
                    textAlign = TextAlign.Justify
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxHeight(1f).weight(.2f).background(Color(0x7A498307))
                .clickable{
                    onNav()
                },
            contentAlignment = Alignment.Center
        ){
            Text("Go")
        }
    }
    Spacer(Modifier.height(10.dp))
}



@Composable
fun RecentDetections(){

}

