package com.example.myapplication.Screens.Login.home

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.example.myapplication.DTO.FertilizerRecommendModel
import com.example.myapplication.DTO.PredictionFerReq
import com.example.myapplication.DTO.PredictionFerRes
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.DataBase.FertilizerSave
import com.example.myapplication.R
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.userDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun FertilizerScreen(){
    val input = remember { mutableStateOf<PredictionFerReq?>(null) }
    val show = remember { mutableStateOf(false) }
    val result = remember { mutableStateOf<FertilizerRecommendModel?>(null) }
    val info = remember { mutableStateOf(false) }
    if (show.value){
        ResultsPageFer(show,result,input)
    }
    val scale = remember { Animatable(0f) } // Initial scale set to 0
    LaunchedEffect(info.value) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing) // Smooth scaling
        )
    }
    if (info.value){
        PopUpScreenFertilizer(info)
        Box(Modifier.graphicsLayer(
            scaleX = scale.value,
            scaleY = scale.value,
            transformOrigin = TransformOrigin(1f, 0f)
        ).fillMaxSize().background(Color(0x70282828)).zIndex(1f))
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 10.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .fillMaxHeight(.7f)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.Center)
                .background(Color(0xFFD1E6D1))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text("Fertilizer", fontSize = 20.sp,)
            }
            AutoFertilizer(show,result,input)
        }
        TopBarFer(info)
    }

}

@Composable
fun PopUpScreenFertilizer(info: MutableState<Boolean>, ) {
    Log.d("TAG", "PopUpScreenFertilizer: ")
    val scale = remember { Animatable(0f) } // Initial scale set to 0
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing) // Smooth scaling
        )
    }
    Popup(
        alignment = Alignment.TopEnd,
        onDismissRequest = { info.value = false },
        offset = _root_ide_package_.androidx.compose.ui.unit.IntOffset(x=-100,y=150)
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    transformOrigin = TransformOrigin(1f, 0f)
                )
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.5f)
                .clip(RoundedCornerShape(15.dp)) // Enhanced roundness
                .background(Color(0xFFE8F5E9)) // Light green background for consistency // Add a green border
                .padding(5.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {
                // Header Text
                Text(
                    text = "🌱 Crop Recommendation Factors:",
                    modifier = Modifier.padding(bottom = 12.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50) // Green header
                )

                // Information Texts
                val factors = listOf(
                    "Nitrogen (N): Promotes leaf and stem growth. Essential for chlorophyll production.",
                    "Phosphorus (P): Encourages root development and reproductive growth.",
                    "Potassium (K): Improves overall plant health, resistance, and quality.",
                )

                factors.forEach { text ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = text,
                            fontSize = 16.sp,
                            color = Color.Black, // Green text
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TopBarFer(info: MutableState<Boolean>) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(end = 10.dp, start = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .clickable {
                    GlobalStates.globalStates.navController?.navigate("home") {
                        popUpTo("fertilizer", { inclusive = true })
                    }
                },

            ){
            Icon(painter = painterResource(R.drawable.go_back_svgrepo_com), contentDescription = "",
                modifier = Modifier.scale(.6f)
            )
        }
        Text("Recommendation")
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .clickable {

                },

            ){
            Icon(painter = painterResource(R.drawable.info_circle_svgrepo_com), contentDescription = "",
                modifier = Modifier
                    .scale(.6f)
                    .clickable {
                        info.value = true
                    }
            )
        }
    }
}

@Composable
fun AutoFertilizer(
    show: MutableState<Boolean>,
    result: MutableState<FertilizerRecommendModel?>,
    input: MutableState<PredictionFerReq?>
) {
    var n = remember { mutableStateOf("") }
    var p = remember { mutableStateOf("") }
    var k = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Spacer(Modifier.height(10.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD1E6D1))// Soft green background

    ) {
        // Input Fields for Nitrogen, Phosphorus, and Potassium
        listOf("Nitrogen" to n, "Phosphorus" to p, "Potassium" to k).forEach { (label, value) ->
            OutlinedTextField(
                value = value.value,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*(\\.\\d*)?\$")) && value.value.length < 3) {
                        value.value = newValue
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .clip(RoundedCornerShape(10.dp)) ,// Spacing between fields
                label = { Text(label) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    cursorColor = Color(0xFF627362),
                    focusedLabelColor = Color.Black
                )
            )
            Spacer(Modifier.height(20.dp))
        }

        // Warning Text
        Text(
            text = "Warning: The accuracy of fertilizer recommendations may vary based on available data and local variations.",
            fontSize = 16.sp,
            color = Color.Red,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Spacer(Modifier.height(30.dp))

        // Submit Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    scope.launch {
                        // Validate input
                        if (n.value == "" || p.value == "" || k.value == "") {
                            SnackBar.s.showSnackbar("All fields are mandatory")
                            return@launch
                        }
                        val req = PredictionFerReq(
                            n.value.toDouble(),
                            p.value.toDouble(),
                            k.value.toDouble()
                        )
                        val res = withContext(Dispatchers.IO) {
                            getFertilizer(req)
                        }
                        result.value = res.recommend
                        if (result.value != null) {
                            withContext(Dispatchers.IO) {
                                DataBaseObject.INSTANCE?.fertilizerDao()?.save(
                                    FertilizerSave().apply {
                                        username = userDetail.username
                                        timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                                            Locale.getDefault()).apply {
                                            timeZone = TimeZone.getTimeZone("UTC")
                                        }.format(Date())
                                        this.input = req
                                        this.result = result.value
                                    }
                                )
                            }
                            input.value = req
                        }
                        show.value = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C9A6C)) // Green Button
            ) {
                Text("Proceed", color = Color.White)
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPageFer(
    show: MutableState<Boolean>,
    result: MutableState<FertilizerRecommendModel?>,
    input: MutableState<PredictionFerReq?>
) {
    val state= rememberModalBottomSheetState()
    val translate = remember { mutableStateOf(false) }
    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = {
            show.value=false
        },
        modifier = Modifier.fillMaxSize()
    ) {
        when(result.value){
            null->{
                Unable(listOf(
                    "Check Internet Connection",
                    "Server May be offline",
                    "Retry again"
                ))
            }
            else -> {
                ShowFertilizerRecommendation(result.value,input.value)
            }
        }
    }
}



suspend fun getFertilizer(req: PredictionFerReq): PredictionFerRes{
    return try{
        Retrofit.api.predictFertilizer(req)
    }catch (e:Exception){
        PredictionFerRes(null,false)
    }
}

