package com.example.myapplication.Screens.Login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.DTO.LoginReq
import com.example.myapplication.DTO.LoginRes
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.SharedPreference
import com.example.myapplication.singleton.userDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController){
    var username = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var enabled = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxHeight(.9f)
        .fillMaxWidth()
        .border(
            width = 0.dp,
            color = Color.Transparent,
            shape = RoundedCornerShape(10.dp)
        )
        .clip(RoundedCornerShape(10.dp))
        .background(color = Color(0xCE627362))
        .padding(10.dp)
    ) {
        Text(
            text = "Login",
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xD5FFFFFF)
        )
        Spacer(Modifier.height(50.dp))
        GetTextFieldlogin("username", username, false)

        Spacer(Modifier.height(50.dp))
        GetTextFieldlogin("Password",password,true)
        Spacer(Modifier.height(50.dp))
        Row (horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Button(
                onClick =  {
                    enabled.value=false
                    coroutineScope.launch{
                        Log.d("TAG","clicked")
                        var res= loginSubmit(username.value,password.value)
                        when(res.status){
                            ""->{
                                SnackBar.showSnack("Something went wrong", SnackbarDuration.Short)
                            }
                            "201"->{
                                SnackBar.showSnack("Missing Fields", SnackbarDuration.Short)
                            }
                            "203"->{
                                SnackBar.showSnack("Invalid Credentials", SnackbarDuration.Short)
                            }
                            "204"->{
                                SnackBar.showSnack("Verification pending", SnackbarDuration.Short)
                                navController.navigate("otp/${res.otpId}")
                            }
                            else->{
                                userDetail.jwt= res.jwt.toString()
                                userDetail.username=res.username.toString()
                                userDetail.principal = withContext(Dispatchers.IO){
                                    DataBaseObject.INSTANCE?.userDao()?.getPrincipal(
                                        res.username.toString()
                                    )
                                }
                                saveUserToDB(username.value, res.jwt.toString())
                                Log.d("TAG", "Login: navigating")
                                navController.navigate("home")
                            }
                        }
                        enabled.value=true
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
                Text("Login")
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Forgot password?",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.clickable{
                navController.navigate("reset")
            }
        )
    }
}


suspend fun loginSubmit(username:String, password:String): LoginRes{
    return try {
        Retrofit.api.login(LoginReq(username,password))
    } catch (e: Exception){
        Log.d("Login error", "loginSubmit: ${e.message}")
        return LoginRes()
    }
}



fun saveUserToDB(username: String, jwt: String){
    SharedPreference.saveData("username",username)
    SharedPreference.saveData("jwt",jwt)
}


@Composable
fun GetTextFieldlogin(label: String, text: MutableState<String>, bool: Boolean){
    TextField(
        value = text.value,
        onValueChange = { newValue ->
                text.value = newValue
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp)),
        label = {Text(label)},
        visualTransformation = if (!bool) VisualTransformation.None else PasswordVisualTransformation(),
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