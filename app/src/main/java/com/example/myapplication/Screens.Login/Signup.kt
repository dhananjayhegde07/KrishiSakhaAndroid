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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.DTO.AddressDTO
import com.example.myapplication.DTO.SignUpReq
import com.example.myapplication.DTO.SignUpRes
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import com.google.gson.Gson
import kotlinx.coroutines.launch
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
            visualTransformation = PasswordVisualTransformation(),
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
        Spacer(Modifier.height(50.dp))

        TextField(value = repassword.value, onValueChange = {
            repassword.value = it},
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
                text = "re-enter password",
                color = Color(0xD5FFFFFF)
            )}
        )

        Spacer(Modifier.height(50.dp))
        TextField(value = email.value, onValueChange = {
            email.value = it},
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
                text = "Email",
                color = Color(0xD5FFFFFF)
            )}
        )

        Spacer(Modifier.height(50.dp))
        TextField(value = phone.value, onValueChange = {
            phone.value = it},
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
                text = "Phone number",
                color = Color(0xD5FFFFFF)
            )}
        )

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

                        // Re-enable the button after the coroutine finishes
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
                Text("Login")
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(value = state.value, onValueChange = {
                state.value = it},
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
                    .clip(RoundedCornerShape(5.dp))
                    .weight(.45f),
                label ={Text(
                    text = "State",
                    color = Color(0xD5FFFFFF)
                )}
            )
            Spacer(modifier = Modifier
                .width(5.dp)
                .weight(.1f))
            TextField(value = dist.value, onValueChange = {
                dist.value = it},
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
                    .clip(RoundedCornerShape(5.dp))
                    .weight(.45f),
                label ={Text(
                    text = "District",
                    color = Color(0xD5FFFFFF)
                )}
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        TextField(value = pin.value, onValueChange = {
            pin.value = it},
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,

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
                text = "PIN",
                color = Color(0xD5FFFFFF)
            )}
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