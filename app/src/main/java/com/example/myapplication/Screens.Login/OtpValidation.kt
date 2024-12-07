package com.example.myapplication.Screens.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.DTO.OtpReq
import com.example.myapplication.DTO.OtpRes
import com.example.myapplication.DTO.ResendOtpReq
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(
    navController: NavController,
    otpId: String
) {
    var otp = remember { mutableStateOf("") }
    val currentId=remember { mutableStateOf(otpId) }
    var isSubmitting = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope=rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1FDF0)), // Light green background
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .shadow(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Verify OTP",
                color =  Color(0xFF388E3C),
                fontWeight = FontWeight.Bold,

                modifier = Modifier.padding(top = 24.dp)
            )

            Text(
                text = "Please enter the OTP sent to your registered email.",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // OTP Input Field
            OutlinedTextField(
                value = otp.value,
                onValueChange = { otp.value=it },
                label = { Text("OTP") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = TextFieldDefaults.colors(

                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Verify Button
            Button(
                onClick = {
                    if (otp.value.length == 6) {
                        isSubmitting.value = true
                        scope.launch(Dispatchers.IO){
                            val res=validate(otp.value,currentId.value)
                            if (res==null) return@launch
                            navController.navigate("login")
                        }
                        isSubmitting.value = false
                    }
                },
                colors = ButtonColors(
                    containerColor = Color(0xFF388E3C),
                    contentColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified,
                    disabledContentColor = Color.Unspecified,
                ),
                enabled = !isSubmitting.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

            ) {
                Text(
                    text = if (isSubmitting.value) "Verifying..." else "Verify",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Resend OTP
            TextButton(onClick = {
                scope.launch(Dispatchers.IO){
                    try {
                        val data = Retrofit.api.resendOtp(ResendOtpReq(otpId))
                        currentId.value=data.otpId
                        withContext(Dispatchers.Main){
                            SnackBar.showSnack("OTP sent successfully", SnackbarDuration.Short)
                        }
                    }catch (e: Exception){
                        withContext(Dispatchers.Main){
                            SnackBar.showSnack("Unable to send Otp", SnackbarDuration.Short)
                        }
                    }
                }
            }) {
                Text(
                    text = "Resend OTP",
                    color = Color(0xFF388E3C), // Green color
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Preview( showBackground = true)
@Composable
fun preview(){

}


suspend fun validate(otp:String,otpId:String): OtpRes?{
    try {
        return Retrofit.api.validate(OtpReq(otp,otpId))
    }catch (e: HttpException){
        withContext(Dispatchers.Main){
            SnackBar.showSnack("Invalid OTP", SnackbarDuration.Short)
        }
        return null
    }
    catch (e:IOException){
        withContext(Dispatchers.Main){
            SnackBar.showSnack("Check your internet Connection", SnackbarDuration.Short)
        }
        return null
    }
    catch (e: Exception){
        return null
    }
}
