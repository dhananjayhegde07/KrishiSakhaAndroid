package com.example.myapplication.Screens.Login.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.myapplication.singleton.SocketManager

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ChatScreen(){
    val connected=remember { mutableStateOf(false) }
    var list=remember { mutableStateListOf<String>() }
    DisposableEffect(Unit) {
        SocketManager.init();
        SocketManager.socket?.on("connect") {
            connected.value=true
        }

        SocketManager.socket?.on("disconnect") {
            connected.value=false
        }

        SocketManager.socket?.on("msg"){args->
            if (args.size!=0){
                list.add(args[0].toString())
            }
            Log.d("TAG", "ChatScreen: ${args[0]}")
            Log.d("TAG", "ChatScreen: ${list}")
        }

        onDispose {
            SocketManager.disconnect()
        }
    }
    if (connected.value){
        Column {
            Text("connected")
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(enabled = true, state = rememberScrollState())
                    .fillMaxWidth()

            ) {
                list.forEach({item->
                    Text(item)
                })
            }
            Button(onClick = {
                SocketManager.socket?.emit("heyy")
            }) { }
        }
    }
    else {
        Log.d("TAG", "bottomNav: not connected")
    }
}

