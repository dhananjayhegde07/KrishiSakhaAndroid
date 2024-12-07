package com.example.myapplication.Screens.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.DTO.ResetInitReq
import com.example.myapplication.DTO.ResetReq
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.userDetail
import kotlinx.coroutines.launch


@Composable
fun ResetScreen(){
    val username = remember { mutableStateOf(userDetail.username) }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val otp = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val otpID = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {

    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(.8f).fillMaxHeight(.5f).clip(RoundedCornerShape(10.dp)).background(Color(0x8AA19F9F))
                .padding(20.dp)
        ) {
            getResetTextField(username,"username")
            getResetTextField(password,"password")
            getResetTextField(confirmPassword,"confirm password")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedTextField(
                    value = otp.value,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*(\\.\\d*)?\$")) ) {
                            otp.value = newValue
                        }
                    },
                    modifier = Modifier
                        .weight(.6f)
                        .clip(RoundedCornerShape(10.dp)),
                    label = {Text("Otp")},
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
                Button(
                    modifier = Modifier.weight(.4f),
                    onClick = {
                        scope.launch{
                            try {
                                otpID.value=Retrofit.api.resetInit(ResetInitReq(username.value)).otpID
                                SnackBar.showSnack("Otp Sent", SnackbarDuration.Short)
                            }catch (http: retrofit2.HttpException){
                                if(http.code()==404){
                                    SnackBar.showSnack("Invalid User", SnackbarDuration.Short)
                                    return@launch
                                }
                                SnackBar.showSnack("Unable to complete request", SnackbarDuration.Short)
                            }catch (e:Exception){
                                SnackBar.showSnack("Unable to complete request", SnackbarDuration.Short)
                            }
                        }
                    }
                ) {
                    Text("send")
                }
            }
            Row {
                Button(
                    enabled = otpID.value!="",
                    onClick = {
                        GlobalStates.globalStates.loading()
                        scope.launch{
                            try {
                                Retrofit.api.reset(
                                    ResetReq(username.value,password.value,otpID.value,otp.value)
                                )
                                GlobalStates.globalStates.notLoading()
                                SnackBar.showSnack("Reset Successful", SnackbarDuration.Short)

                                GlobalStates.globalStates.navController?.popBackStack()
                            } catch (e: Exception){
                                GlobalStates.globalStates.notLoading()
                                SnackBar.showSnack("Unable to complete request", SnackbarDuration.Short)

                            }
                        }
                    }
                ) {
                    Text("Reset")
                }
            }
        }
    }

}

@Composable
fun getResetTextField(msg: MutableState<String>, label: String) {
    OutlinedTextField(
        value = msg.value,
        onValueChange = { newValue ->
            msg.value=newValue
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        label = {Text(label)},
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