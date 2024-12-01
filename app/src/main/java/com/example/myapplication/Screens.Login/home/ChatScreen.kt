package com.example.myapplication.Screens.Login.home

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.DTO.Messege
import com.example.myapplication.R
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.SocketManager
import com.example.myapplication.singleton.translateEnglishToKannada
import com.example.myapplication.singleton.userDetail
import com.example.myapplication.utils.Officials
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ChatScreen(){
    val connected= rememberSaveable { mutableStateOf(true) }
    val official=rememberSaveable { mutableStateOf<Officials?>(null) }
    val init =rememberSaveable { mutableStateOf(false) }
    val chatId=rememberSaveable { mutableStateOf("") }
    LaunchedEffect(Unit) {
        if (init.value) return@LaunchedEffect
        Log.d("TAG", "ChatScreen: ${init.value}")
        GlobalStates.globalStates.chatList.clear()
        try {
            val res= withContext(Dispatchers.IO){ Retrofit.api.getOfficial()}
            Log.d("TAG", "ChatScreen: ${res.chatID}")
            official.value=res.official
            chatId.value=res.chatID.toString()
            SocketManager.socket?.on("connect"){
                connected.value=true
            }
            SocketManager.socket?.on("disconnect") {
                connected.value = false
            }
            init.value=true
        } catch (e: Exception) {
            Log.e("TAG", "ChatScreen: ${e.message}", )
        }
    }
    when {
        official.value != null -> ChatScreenContent(official, connected,chatId)
        official.value == null -> Unavailable()
    }
}




@Composable
fun ChatScreenContent(
    official: MutableState<Officials?>,
    connected: MutableState<Boolean>,
    chatId: MutableState<String>,
) {
    val disp = remember { mutableStateOf(false) }
    var chatList= GlobalStates.globalStates.chatList
    DisposableEffect(Unit) {
        SocketManager.socket?.on("msg") { args->
            val now= Gson().fromJson<Messege>(args[0].toString(), Messege::class.java)
            chatList.add(now)
        }
        disp.value = true
        onDispose {
            SocketManager.socket?.off("msg")
        }
    }
    if (!disp.value) return
    val msg = remember { mutableStateOf("") }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0x12F1CE50))
    ) {
        Box(
            Modifier
                .weight(.09f)
                .fillMaxSize()
                .background(Color(0x6184B661))){
            TopChatBar(official)
        }
        Box(
            Modifier
                .weight(.82f)
                .fillMaxSize())
        {
            MesseggeList()
        }

        Box(
            Modifier
                .weight(.1f)
                .fillMaxSize()
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxSize()
            ) {
                MenuBox()
                InputField(msg)
                Box(
                    Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable {
                            val current = Messege(
                                from = userDetail.username,
                                to = official.value?.pin.toString(),
                                msg = msg.value,
                                index = 0
                            )
                            sendMSg(MessageDTO(chatId.value, current))
                            if (msg.value != "") GlobalStates.globalStates.chatList.add(current)
                            msg.value = ""
                        }
                    , contentAlignment = Alignment.Center

                ){
                    Icon(painter = painterResource(R.drawable.send_square_svgrepo_com), contentDescription = "")
                }
            }
        }
    }
}

@Composable
fun MesseggeList() {
    val screenwidth= LocalConfiguration.current.screenWidthDp
    val chatlist= GlobalStates.globalStates.chatList
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(7.dp)
    ){
        items(chatlist.size){
            val transText = remember { mutableStateOf("") }
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (chatlist[it].from==userDetail.username) Arrangement.End else Arrangement.Start
            ){
                Icon(painter = painterResource(R.drawable.translate_svgrepo_com), contentDescription = "",
                    modifier = Modifier
                        .height(20.dp)
                        .clickable {
                            if (transText.value!=""){
                                transText.value=""
                                return@clickable
                            }
                            translateEnglishToKannada(chatlist[it].msg,
                                { text ->
                                    transText.value=text
                                },
                                { err ->
                                    Log.d("TAG", "MesseggeList: $err")
                                }
                            )
                        }
                )
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .heightIn(min = 50.dp)
                        .widthIn(min = 50.dp, max = (0.7 * screenwidth).dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFCBCBCB))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = if(transText.value=="")chatlist[it].msg else transText.value,
                        fontSize = 20.sp,
                        )
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun Unavailable(){
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Not Available")
    }
}

@Composable
fun NotConnected() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Not Connected to Server")
    }
}

fun sendMSg(messege: MessageDTO){
    if (messege.message.msg=="") return
    Log.d("TAG", "sendMSg: ${messege}")
    SocketManager.socket?.emit("send", Gson().toJson(messege))
}


@Composable
fun TopChatBar(official: MutableState<Officials?>) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 5.dp, end = 10.dp)
    ){
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .clickable {
                    GlobalStates.globalStates.navController?.navigate("home") {
                        popUpTo("chat", { inclusive = true })
                    }
                },

        ){
            Icon(painter = painterResource(R.drawable.go_back_svgrepo_com), contentDescription = "",
                modifier = Modifier.scale(.6f)
            )
        }
        Text(text = official.value?.name.toString(), fontSize = 20.sp)
        Icon(painter = painterResource(R.drawable.info_circle_svgrepo_com), contentDescription = ""
            , modifier = Modifier.height(30.dp)
        )
    }
}



@Composable
fun MenuBox(){
    Box(
        Modifier
            .height(40.dp)
            .width(40.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .clickable {

            }
    ){
        Icon(painter = painterResource(R.drawable.menu_dots_circle_svgrepo_com), contentDescription = "",
            modifier = Modifier.scale(.6f)
        )
    }
}



@Composable
fun InputField(msg: MutableState<String>) {

        OutlinedTextField(
            value = msg.value,
            onValueChange = { msg.value = it },
            modifier = Modifier
                .fillMaxWidth(.8f)
                .height(60.dp)
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color(0xFF627362),

            ),
            shape = OutlinedTextFieldDefaults.shape.apply {

            },
            placeholder = {Text("Type here...")},
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)

        )
}

