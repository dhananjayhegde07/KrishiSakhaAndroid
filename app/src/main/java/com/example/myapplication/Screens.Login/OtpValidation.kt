package com.example.myapplication.Screens.Login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.DTO.OtpReq
import com.example.myapplication.retrofit.Retrofit
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


@Composable
fun OtpValidation(navController: NavController?, otpId: String?){
    if (otpId==null){
        navController?.navigate("login",)
        return
    }
    var otp= remember{ mutableStateOf("") }
    var coroutineScope= rememberCoroutineScope()
    var enabled= remember{ mutableStateOf(true) }
    Column(
        verticalArrangement =  Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            fontSize = 15.sp,
            text = "Enter OTP"
        )
        TextField(
            value = otp.value,
            onValueChange = {otp.value=it}
        )
        Button(enabled = enabled.value,onClick = {
            enabled.value=false
            coroutineScope.launch{
                val res=validate(otp.value,otpId)
                enabled.value=true
//                navController.navigate("home")
            }
        }) { Text("submit")}
    }
}

@Preview( showBackground = true)
@Composable
fun preview(){
    OtpValidation(rememberNavController(),"123")
}


suspend fun validate(otp:String,otpId:String){
    try {
        Retrofit.api.validate(OtpReq(otp,otpId))
    }catch (e: HttpException){

    }
    catch (e:IOException){

    }
    catch (e: Exception){

    }
}
