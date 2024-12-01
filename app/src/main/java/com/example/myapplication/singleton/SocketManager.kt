package com.example.myapplication.singleton

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

object SocketManager {
    var socket: Socket? = null

    fun init(){
        try {
            val options=IO.Options().apply {
                reconnectionAttempts=10
                auth=mapOf("token" to userDetail.jwt)
            }
            socket=IO.socket("http://192.168.15.253:3000/",options)
            socket?.connect()
            Log.d("TAG", "init: ${socket?.connected()}")
            // Optionally, handle connect and disconnect events

            socket?.on("connect_error"){args->
                Log.d("TAG", "init: ${args[0]}")
                Log.d("TAG", "init: reconnecting")
                val authMap = options.auth?.toMutableMap() ?: mutableMapOf()
                authMap["token"] = userDetail.jwt
                options.auth = authMap
            }
            socket?.on("disconnect") {
                Log.d("TAG", "init: Disconnected from the server")
            }
        }
        catch (e: Exception){
            Log.e("TAG", "init: ${e.toString()}", )
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket?.close()
    }

}