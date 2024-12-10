package com.example.myapplication.Screens.Login.home

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.example.myapplication.DTO.CropRecReq
import com.example.myapplication.DTO.CropRecommendationModel
import com.example.myapplication.DTO.CropRecommendationReq
import com.example.myapplication.DTO.CropRecommendationRes
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.DataBase.RecommendationSave
import com.example.myapplication.R
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.userDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun CropRecommendScreen(){
    val auto = remember { mutableStateOf(true) }
    val show = remember { mutableStateOf(false) }
    val result = remember { mutableStateOf<CropRecommendationModel?>(null) }
    val info = remember { mutableStateOf(false) }
    val input = remember { mutableStateOf<CropRecReq?>(null) }
    if (show.value){
        ResultsPagecrop(show,result,input)
    }
    val scale = remember { Animatable(0f) } // Initial scale set to 0
    LaunchedEffect(info.value) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing) // Smooth scaling
        )
    }
    if (info.value){
        PopUpScreenRecommendation(info)
        Box(
            Modifier
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    transformOrigin = TransformOrigin(1f, 0f)
                )
                .fillMaxSize()
                .background(Color(0x70282828))
                .zIndex(1f))
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.Center)
                .background(Color(0xFFE8F5E9)) // Light green background for the card
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(Color(0xFF4CAF50)) // Green header bar
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .background(
                            if (auto.value) Color(0xFF81C784) // Active state: lighter green
                            else Color(0xFF4CAF50) // Inactive state: header green
                        )
                        .clickable { auto.value = true }
                ) {
                    Text(
                        "Auto",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .background(
                            if (!auto.value) Color(0xFF81C784) // Active state: lighter green
                            else Color(0xFF4CAF50) // Inactive state: header green
                        )
                        .clickable { auto.value = false }
                ) {
                    Text(
                        "Manual",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            when (auto.value) {
                true -> AutoRecmmend(show, result, auto, input)
                false -> ManualRecommend(show, result, auto, input)
            }
        }
        TopBarRec(info)
    }

}

@Composable
fun PopUpScreenRecommendation(info: MutableState<Boolean>) {
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
        offset = IntOffset(x = -100, y = 150)
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
                    "Soil Nutrients: NPK levels and pH influence crop growth and yield.",
                    "Climate: Factors like temperature, humidity, and rainfall determine crop suitability.",
                    "Water Requirements: Ensure the crop's water needs align with local conditions.",
                    "Growing Season: Choose crops suitable for the season and weather patterns."
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPagecrop(
    show: MutableState<Boolean>,
    result: MutableState<CropRecommendationModel?>,
    input: MutableState<CropRecReq?>
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
                ),painterResource(R.drawable.error_inspect_ios11_svgrepo_com))
            }
            else -> {
                ShowCropRecommendation(result.value,input.value)
            }
        }
    }
}

@Composable
fun GetPoints(list: List<String>?) {
    Log.d("TAG", "GetPoints: $list")
    list?.forEach {
        Text(it)
    }
}


@Composable
fun ManualRecommend(
    show: MutableState<Boolean>,
    result: MutableState<CropRecommendationModel?>,
    auto: MutableState<Boolean>,
    input: MutableState<CropRecReq?>
) {
    var n = remember { mutableStateOf("") }
    var p = remember { mutableStateOf("") }
    var k = remember { mutableStateOf("") }
    var ph = remember { mutableStateOf("") }
    var temp = remember { mutableStateOf("") }
    var humidity = remember { mutableStateOf("") }
    var rainfall = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .pointerInput(Unit) {
                coroutineScope {
                    detectHorizontalSwipe(
                        onSwipeLeft = {

                        },
                        onSwipeRight = {
                            Log.d("TAG", "AutoRecmmend: ")
                            auto.value = true
                        }
                    )
                }
            }
            .verticalScroll(state = rememberScrollState(), enabled = true)
    ) {
        Spacer(Modifier.height(10.dp))
        Text("All fields are mandatory")
        Spacer(Modifier.height(10.dp))
        Row (
            Modifier.fillMaxWidth()
        ){
            Box(
                Modifier.weight(.5f)
            ){
                GetTextField(n,"Nitrogen")
            }
            Spacer(Modifier.width(5.dp))
            Box(
                Modifier.weight(.5f)
            ){
                GetTextField(p, "Phosphorus")
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier.fillMaxWidth()
        ) {
            Box(
                Modifier.weight(.5f)
            ){
                GetTextField(k, "Potassium")
            }
            Spacer(Modifier.width(5.dp))
            Box(
                Modifier.weight(.5f)
            ){
                GetTextField(ph, "Ph level")
            }

        }
        Spacer(Modifier.height(10.dp))
        GetTextField(temp,"Temperature")
        Spacer(Modifier.height(10.dp))
        GetTextField(humidity,"Humidity")
        Spacer(Modifier.height(10.dp))
        GetTextField(rainfall,"Rainfall")
        Spacer(Modifier.height(10.dp))
        Text("Incorrect or outdated input data may result in inaccurate crop recommendations. Double-check soil, climate, and NPK values for reliable suggestions.", fontSize = 16.sp,color = Color.Red)
        Spacer(Modifier.height(10.dp))
        var enabled = remember { mutableStateOf(true) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                enabled = enabled.value,
                onClick = {
                    enabled.value=false
                    val list = listOf(n.value,p.value,k.value,ph.value,temp.value,humidity.value,rainfall.value)
                    if(list.all {it.toDoubleOrNull()==null}){
                        enabled.value=true
                        scope.launch{
                            SnackBar.s.showSnackbar("Invalid Data")
                        }
                        return@Button
                    }
                    scope.launch{
                        GlobalStates.globalStates.loading()
                        val req = CropRecReq(
                            n.value.toDouble(),
                            p.value.toDouble(),
                            k.value.toDouble(),
                            temp.value.toDouble(),
                            humidity.value.toDouble(),
                            ph.value.toDouble(),
                            rainfall.value.toDouble()
                        )
                        result.value= withContext(Dispatchers.IO){
                            getRecommendation(CropRecommendationReq(true, req))?.recommend
                        }
                        GlobalStates.globalStates.notLoading()
                        if (result.value!=null){
                            withContext(Dispatchers.IO){
                                DataBaseObject.INSTANCE?.recommendationDao()?.save(
                                    RecommendationSave().apply {
                                        this.username = userDetail.username
                                        this.timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                                            Locale.getDefault()).apply {
                                            timeZone = TimeZone.getTimeZone("UTC")
                                        }.format(Date())
                                        this.input = req
                                        this.result = result.value
                                    }
                                )
                            }
                            input.value=req
                        }
                        show.value=true
                        enabled.value=true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Green button
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Proceed", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AutoRecmmend(
    show: MutableState<Boolean>,
    result: MutableState<CropRecommendationModel?>,
    auto: MutableState<Boolean>,
    input: MutableState<CropRecReq?>
) {
    val pin = remember { mutableStateOf(userDetail.principal?.pin ?: "") }
    val scope = rememberCoroutineScope()
    val enabled = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F5E9)) // Light green background for consistency
            .pointerInput(Unit) {
                coroutineScope {
                    detectHorizontalSwipe(
                        onSwipeLeft = {
                            Log.d("TAG", "AutoRecmmend: Swipe Left")
                            auto.value = false
                        },
                        onSwipeRight = {
                            // Add action if needed
                        }
                    )
                }
            }
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        // Pin code entry
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = pin.value,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*(\\.\\d*)?\$"))) {
                        pin.value = newValue
                    }
                },
                enabled=false,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(10.dp)),
                label = { Text("Pin Code") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledContainerColor = Color.White,
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    cursorColor = Color(0xFF4CAF50), // Green cursor
                    focusedLabelColor = Color(0xFF4CAF50) // Green label
                )
            )
        }
        Spacer(Modifier.height(16.dp))

        // Informative text
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                "Provides crop recommendations based on soil NPK values, climate, and environmental data to maximize yield.",
                fontSize = 16.sp,
                color = Color(0xFF4CAF50), // Green text
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Factors in real-time weather conditions, such as rainfall and temperature, for optimal crop selection.",
                fontSize = 16.sp,
                color = Color(0xFF4CAF50),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Adapts recommendations to local soil characteristics using pin code-based data retrieval.",
                fontSize = 16.sp,
                color = Color(0xFF4CAF50),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Warning: Recommendations are based on available data and may not consider unexpected local conditions or recent soil treatments. Always consult an expert for critical decisions.",
                fontSize = 16.sp,
                color = Color.Red,
                lineHeight = 20.sp
            )
        }

        // Proceed button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                enabled = enabled.value,
                onClick = {
                    enabled.value = false
                    scope.launch {
                        GlobalStates.globalStates.loading()
                        val req = CropRecommendationReq(false, null)
                        result.value = withContext(Dispatchers.IO) {
                            getRecommendation(req)?.recommend
                        }
                        if (result.value != null) {
                            withContext(Dispatchers.IO) {
                                DataBaseObject.INSTANCE?.recommendationDao()?.save(
                                    RecommendationSave().apply {
                                        username = userDetail.username
                                        timestamp = SimpleDateFormat(
                                            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                                            Locale.getDefault()
                                        ).apply {
                                            timeZone = TimeZone.getTimeZone("UTC")
                                        }.format(Date())
                                        this.input = null
                                        this.result = result.value
                                    }
                                )
                            }
                            input.value = req.data
                        }
                        enabled.value = true
                        GlobalStates.globalStates.notLoading()
                        show.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Green button
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Proceed", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun TopBarRec(info: MutableState<Boolean>) {
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
fun GetTextField(text: MutableState<String>, label: String) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { newValue ->
            if (newValue.matches(Regex("^\\d*(\\.\\d*)?\$")) ) {
                text.value = newValue
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        label = {Text(label)},
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


suspend fun androidx.compose.ui.input.pointer.PointerInputScope.detectHorizontalSwipe(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    detectHorizontalDragGestures { change, dragAmount ->
        change.consume() // Consume the gesture to prevent further propagation
        when {
            dragAmount < -50 -> onSwipeLeft()
            dragAmount > 50 -> onSwipeRight()
        }
    }
}

suspend fun getRecommendation(data: CropRecommendationReq): CropRecommendationRes?{
    return try {
        Retrofit.api.predictionCrop(data)
    }catch (e: Exception){
        println(e)
        null
    }
}
