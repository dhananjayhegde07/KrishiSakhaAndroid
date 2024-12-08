package com.example.myapplication.Screens.Login.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.example.myapplication.DTO.CLoseChatSocket
import com.example.myapplication.DTO.CloseChatReq
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.DTO.Messege
import com.example.myapplication.R
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.SocketManager
import com.example.myapplication.singleton.translateEnglishToKannada
import com.example.myapplication.singleton.userDetail
import com.example.myapplication.utils.Officials
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ChatScreen(){
    val connected= rememberSaveable { mutableStateOf(true) }
    val official=rememberSaveable { mutableStateOf<Officials?>(null) }
    val init =rememberSaveable { mutableStateOf(false) }
    val chatId=rememberSaveable { mutableStateOf("") }
    val loading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (init.value) return@LaunchedEffect
        Log.d("TAG", "ChatScreen: ${init.value}")
        loading.value = true
        GlobalStates.globalStates.chatList.clear()
        try {
            val res= withContext(Dispatchers.IO){ Retrofit.api.getOfficial()}
            Log.d("TAG", "ChatScreen: ${res.chatID}")
            official.value=res.official
            chatId.value=res.chatID.toString()
            GlobalStates.globalStates.chatList.addAll(res.chatList)
            SocketManager.socket?.on("connect"){
                connected.value=true
            }
            SocketManager.socket?.on("disconnect") {
                connected.value = false
            }
            init.value=true
        } catch (e: Exception) {
            Log.e("TAG", "ChatScreen: ${e.message}", )
        }finally {
            loading.value=false
        }
    }
    if (loading.value){
        Box(Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text("Loading...", color = Color.Green,)
        }
        return
    }
    when {
        official.value != null -> ChatScreenContent(official, connected,chatId)
        official.value == null -> Unable(listOf(
            "Check Your Internet connection",
            "Server May be offline",
            "Try again After some time"
        ))
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
            val now= Gson().fromJson<MessageDTO>(args[0].toString(), MessageDTO::class.java)
            chatList.add(now.message)
        }
        disp.value = true
        onDispose {
            SocketManager.socket?.off("msg")
        }
    }
    if (!disp.value) return
    val msg = remember { mutableStateOf("") }
    val info = remember { mutableStateOf(false) }
    val close = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    if(info.value){
        ChatInfoPop(info,official,chatId)
        Box(Modifier.fillMaxSize().background(Color(0x70282828)).zIndex(1f))
    }
    if (close.value){
        ChatAlert(
            onClose = {
                scope.launch(Dispatchers.IO){
                    try {
                        Retrofit.api.closeChat(CloseChatReq(chatId.value))

                    }catch (e: Exception){
                        SnackBar.showSnack("Unable to close", SnackbarDuration.Short)
                        return@launch
                    }
                    SocketManager.socket?.emit("close", Gson().toJson(CLoseChatSocket(chatId.value,official.value?.pin.toString())))
                    close.value=false
                    SnackBar.showSnack("Closed", SnackbarDuration.Short)
                    withContext(Dispatchers.Main){
                        GlobalStates.globalStates.navController?.navigate("home"){
                            popUpTo("chat",{inclusive=true})
                        }
                    }
                }
            },
            onCancel = {close.value=false},
        )
    }

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
            TopChatBar(official,info)
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
                MenuBox(close)
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
fun ChatAlert(onClose: () -> Unit,
              onCancel: () -> Unit ){
    AlertDialog(
        containerColor =  Color(0xFFE8F5E9),
        onDismissRequest = onCancel,
        title = {
            Text(
                text = "Close Chat Session?",
            )
        },
        text = {
            Text(
                text = "Are you sure you want to close this chat session?",
            )
        },
        confirmButton = {
            TextButton(onClick = onClose) {
                Text(
                    text = "Close",

                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    text = "Cancel",
                )
            }
        }
    )
}


@Composable
fun ChatInfoPop(
    info: MutableState<Boolean>,
    official: MutableState<Officials?>,
    chatId: MutableState<String>
) {
    val scale = remember { Animatable(0f) } // Initial scale set to 0
    LaunchedEffect(info.value) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    }
    Popup(
        alignment = Alignment.TopEnd,
        onDismissRequest = { info.value = false },
        offset = _root_ide_package_.androidx.compose.ui.unit.IntOffset(x=-100,y=150)
    ) {
        Box(
            modifier = Modifier.graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                transformOrigin = TransformOrigin(1f, 0f)
                )
                .fillMaxWidth(.7f).fillMaxHeight(.5f).clip(RoundedCornerShape(10.dp))
                .background(Color.White).padding(10.dp)
        ){
            Column(modifier = Modifier.padding(16.dp)) {
                // Header: "Chatting with"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Chatting with", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(20.dp))

                // Name Text
                Text("Name: ${official.value?.name ?: ""}", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                // Email Text
                Text("Email: ${official.value?.email ?: ""}", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                // Phone Text
                Text("Phone: ${official.value?.phone ?: ""}", fontSize = 16.sp)
                Spacer(Modifier.height(20.dp))

                // Header: "Session Info"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Session Info", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(20.dp))

                // Chat ID Text
                Text("Chat ID: ${chatId.value}", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))
            }

        }
    }
}

@Composable
fun MesseggeList() {
    val screenwidth= LocalConfiguration.current.screenWidthDp
    val chatlist= GlobalStates.globalStates.chatList
    val listState = rememberLazyListState()

    // Automatically scroll to the bottom whenever the message list changes
    LaunchedEffect(chatlist.size) {
        if (chatlist.isNotEmpty()) {
            listState.animateScrollToItem(chatlist.size - 1)
        }
    }
    LazyColumn (
        state = listState,
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
                        .background(if (chatlist[it].from==userDetail.username) Color(0xD03B9F61) else Color(0xFFCBCBCB))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = if(transText.value=="")chatlist[it].msg else transText.value,
                        fontSize = 20.sp,
                        color = if (chatlist[it].from==userDetail.username) Color.White else Color.Black
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
fun TopChatBar(official: MutableState<Officials?>, info: MutableState<Boolean>) {
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
            , modifier = Modifier.height(30.dp).clickable{
                info.value=true
            }
        )
    }
}



@Composable
fun MenuBox(close: MutableState<Boolean>) {

    Box(
        Modifier
            .height(40.dp)
            .width(40.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .clickable {
                close.value=true
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

