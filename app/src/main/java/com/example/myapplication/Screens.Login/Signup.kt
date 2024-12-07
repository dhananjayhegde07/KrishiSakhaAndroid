package com.example.myapplication.Screens.Login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.DTO.AddressDTO
import com.example.myapplication.DTO.SignUpReq
import com.example.myapplication.DTO.SignUpRes
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.DataBase.User
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(navController: NavController){
    var username = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var repassword = remember { mutableStateOf("") }
    var email=remember { mutableStateOf("") }
    var phone =remember { mutableStateOf("") }
    var state =remember { mutableStateOf("") }
    var dist=remember { mutableStateOf("") }
    var pin = remember { mutableStateOf("") }
    var enabled = remember { mutableStateOf(true) }
    val coroutineScope= rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.9f)
            .border(
                width = 0.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = Color(0xCE627362)
            )
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Signup",
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xD5FFFFFF)
        )
        Spacer(Modifier.height(50.dp))
        GetTextFieldlogin("username",username,false)
        Spacer(Modifier.height(50.dp))
        GetTextFieldlogin("password",password,true)
        Spacer(Modifier.height(50.dp))

        GetTextFieldlogin("Re-password",repassword,true)

        Spacer(Modifier.height(50.dp))
        GetTextFieldlogin("E-mail",email,false)

        Spacer(Modifier.height(50.dp))
        GetTextFieldlogin("Phone",phone,false)

        Spacer(Modifier.height(50.dp))
        address(state,dist,pin)

        Spacer(Modifier.height(50.dp))

        Row (horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Button(
                onClick = {
                    enabled.value = false // Disable the button immediately
                    coroutineScope.launch {
                        Log.d("TAG", "Starting signup...")

                        // Perform the signup operation
                        val res = signup(
                            SignUpReq(
                                username.value,
                                password.value,
                                repassword.value,
                                email.value,
                                phone.value,
                                AddressDTO(state.value, dist.value, pin.value)
                            )
                        )

                        // Log the response status
                        Log.d("TAG", res.status.toString())

                        // Handle different response cases
                        when (res.status) {
                            null -> {
                                SnackBar.showSnack("Something went Wrong", SnackbarDuration.Short)
                            }
                            "200" -> {
                                SnackBar.showSnack("Username Already Taken", SnackbarDuration.Short)
                            }
                            "201" -> {
                                SnackBar.showSnack("Some Fields are Missing", SnackbarDuration.Short)
                            }
                            else -> {
                                SnackBar.showSnack("Signup Successful", SnackbarDuration.Short)
                            }
                        }
                        withContext(Dispatchers.IO){
                            DataBaseObject.INSTANCE?.userDao()?.insertUser(
                                User().apply {
                                    this.username= username.value
                                    this.password=password.value
                                    this.name=""
                                    this.email=email.value
                                    this.phone = phone.value
                                    this.pin=pin.value
                                }
                            )
                        }
                        enabled.value = true
                        navController.navigate("otp/${res.otpId}")
                    }
                },
                enabled = enabled.value,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(50.dp)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xD5356E28)
                )
            ) {
                Text("Sign Up")
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun address( state: MutableState<String>,  dist:MutableState<String>,  pin:MutableState<String>){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Address",
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xD5FFFFFF)
        )
        Spacer(modifier = Modifier.height(20.dp))
        GetTextFieldlogin("District",dist,false)
        Spacer(modifier = Modifier.height(50.dp))
        GetTextFieldlogin("State",state,false)
        Spacer(modifier = Modifier.height(50.dp))
        TextField(
            value = pin.value,
            onValueChange = { newValue ->
                if (newValue.matches(Regex("^\\d*(\\.\\d*)?\$")) ) {
                    pin.value = newValue
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)),
            label = {Text("Pin")},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color(0xFF627362),
                focusedLabelColor = Color.Black,
            )
        )
    }
}



suspend fun signup(signUpReq: SignUpReq): SignUpRes{
    try{
        return Retrofit.api.signup(signUpReq)
    }
    catch (e: retrofit2.HttpException) {
        // Handle HTTP exceptions (e.g., 4xx and 5xx responses)
        val json=e.response()?.errorBody()?.string()
        var map: Map<String, String>?
        try {
            map= Gson().fromJson(json, Map::class.java)  as Map<String, String>
            Log.e("TAG", "HTTP Exception: ${map["msg"]}")
            return SignUpRes(map["username"],map["status"],map["msg"])
        }catch (e: Exception){
            Log.e("TAG", "HTTP Exception: ${e.message}")
            return SignUpRes()
        }
         // Return a default or empty response
    } catch (e: IOException) {
        // Handle network failures (e.g., no internet connection)
        Log.e("TAG", "Network failure: ${e.message}")
        return SignUpRes() // Return a default or empty response
    } catch (e: Exception) {
        // Handle other types of exceptions
        Log.e("Network Error", "Unexpected error: ${e.message}")
        return SignUpRes() // Return a default or empty response
    }

}