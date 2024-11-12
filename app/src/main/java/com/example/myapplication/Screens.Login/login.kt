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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.DTO.LoginReq
import com.example.myapplication.DTO.LoginRes
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.SharedPreference
import com.example.myapplication.singleton.userDetail
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
        TextField(value = username.value, onValueChange = {
            username.value = it},
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp
            ),
            colors = TextFieldDefaults.textFieldColors(
                // Set the indicator color to transparent
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(5.dp)
                )
                .clip(RoundedCornerShape(5.dp)),
            label ={Text(
                text = "Username",
                color = Color(0xD5FFFFFF)
            )}
            )

        Spacer(Modifier.height(50.dp))
        TextField(value = password.value, onValueChange = {
            password.value = it},
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp
            ),
            visualTransformation = PasswordVisualTransformation()
            ,
            colors = TextFieldDefaults.textFieldColors(
                // Set the indicator color to transparent
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(5.dp)
                )
                .clip(RoundedCornerShape(5.dp)),
            label ={Text(
                text = "Password",
                color = Color(0xD5FFFFFF)
            )}
        )
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
        Spacer(Modifier.height(50.dp))

    }
}


suspend fun loginSubmit(username:String, password:String): LoginRes{
    return try {
        Retrofit.api.login(LoginReq(username,password))
    } catch (e: HttpException){
        Gson().fromJson(e.response()?.errorBody()?.string(), LoginRes::class.java ) ?: LoginRes()
    }
    catch (e: Exception){
        LoginRes()
    }
}


fun saveUserToDB(username: String, jwt: String){
    SharedPreference.saveData("username",username)
    SharedPreference.saveData("jwt",jwt)
}