package com.example.myapplication.Screens.Login.home

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.DTO.CropRecReq
import com.example.myapplication.DTO.CropRecommendationModel
import com.example.myapplication.DTO.CropRecommendationReq
import com.example.myapplication.DTO.CropRecommendationRes
import com.example.myapplication.R
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.userDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CropRecommendScreen(){
    val auto = remember { mutableStateOf(true) }
    val show = remember { mutableStateOf(false) }
    val result = remember { mutableStateOf<CropRecommendationModel?>(null) }
    if (show.value){
        ResultsPagecrop(show,result)
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 10.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(.95f)
                .fillMaxHeight(.8f)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.Center)
                .background(Color(0x5CAFAEAE))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(.5f).fillMaxHeight().background(if (auto.value) Color(
                        0x5C5CE35C
                    ) else Color.Transparent).clickable{
                        auto.value=true
                    }
                ){
                    Text("Auto")
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(.5f).fillMaxHeight().background(if (!auto.value) Color(
                        0x5C5CE35C
                    ) else Color.Transparent).clickable{
                        auto.value=false
                    }
                ){
                    Text("Manual")
                }
            }
            when(auto.value){
                true -> AutoRecmmend(show,result,auto)
                false -> ManualRecommend(show,result,auto)
            }
        }
        TopBarRec()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPagecrop(show: MutableState<Boolean>, result: MutableState<CropRecommendationModel?>) {
    val state= rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = {
            show.value=false
        },
        modifier = Modifier.fillMaxSize()
    ) {
        when(result.value){
            null->{
                Text("unable to Predict")
            }
            else -> {
                Text(result.value?.scientificName.toString())
            }
        }
    }
}

@Composable
fun ManualRecommend(
    show: MutableState<Boolean>,
    result: MutableState<CropRecommendationModel?>,
    auto: MutableState<Boolean>
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
            }.verticalScroll(state = rememberScrollState(), enabled = true)
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
        Text("Incorrect or outdated input data may result in inaccurate crop recommendations. Double-check soil, climate, and NPK values for reliable suggestions.", color = Color.Red)
        Spacer(Modifier.height(10.dp))
        var enabled = remember { mutableStateOf(true) }
        Row {
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
                        result.value= withContext(Dispatchers.IO){
                            getRecommendation(CropRecommendationReq(true, CropRecReq(
                                n.value.toDouble(),
                                p.value.toDouble(),
                                k.value.toDouble(),
                                temp.value.toDouble(),
                                humidity.value.toDouble(),
                                ph.value.toDouble(),
                                rainfall.value.toDouble()
                            )))?.recommend
                        }
                        GlobalStates.globalStates.notLoading()
                        show.value=true
                        enabled.value=true
                    }
                }
            ) {
                Text("Proceed")
            }
        }
    }
}

@Composable
fun AutoRecmmend(
    show: MutableState<Boolean>,
    result: MutableState<CropRecommendationModel?>,
    auto: MutableState<Boolean>
) {
    val pin = remember { mutableStateOf(userDetail.principal?.pin ?: "") }
    val scope = rememberCoroutineScope()
    var enabled = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                coroutineScope {
                    detectHorizontalSwipe(
                        onSwipeLeft = {
                            Log.d("TAG", "AutoRecmmend: ")
                            auto.value = false
                        },
                        onSwipeRight = {

                        }
                    )
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
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
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clip(RoundedCornerShape(10.dp)),
                label = {Text("Pin Code")},
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
        Spacer(Modifier.height(20.dp))
        Column(
            Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Text("Provides crop recommendations based on soil NPK values, climate, and environmental data to maximize yield.", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Factors in real-time weather conditions, such as rainfall and temperature, for optimal crop selection.", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Adapts recommendations to local soil characteristics using pin code-based data retrieval.", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Warning: Recommendations are based on available data and may not consider unexpected local conditions or recent soil treatments. Always consult an expert for critical decisions."
            , fontSize = 16.sp, color = Color.Red)
        }

        Row {
            Button(
                enabled = enabled.value,
                onClick = {
                    enabled.value=false
                    scope.launch{
                        GlobalStates.globalStates.loading()
                        result.value= withContext(Dispatchers.IO){
                            getRecommendation(CropRecommendationReq(false,null))?.recommend
                        }
                        enabled.value=true
                        GlobalStates.globalStates.notLoading()
                        show.value=true
                    }
                }
            ) {
                Text("Proceed")
            }
        }

    }
}

@Composable
fun TopBarRec() {
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
                modifier = Modifier.scale(.6f)
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
