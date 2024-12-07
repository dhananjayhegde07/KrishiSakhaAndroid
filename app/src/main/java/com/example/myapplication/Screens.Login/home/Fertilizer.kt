package com.example.myapplication.Screens.Login.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
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
    val auto = remember { mutableStateOf(true) }
    val show = remember { mutableStateOf(false) }
    val result = remember { mutableStateOf<FertilizerRecommendModel?>(null) }
    val info = remember { mutableStateOf(false) }
    if (show.value){
        ResultsPageFer(show,result)
    }
    if (info.value){
        PopUpScreenFertilizer(info)
        Box(Modifier.fillMaxSize().background(Color(0x70282828)).zIndex(1f))
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
                .background(Color(0x5CAFAEAE))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text("Fertilizer", fontSize = 20.sp,)
            }
            AutoFertilizer(show,result)
        }
        TopBarFer(info)
    }

}

@Composable
fun PopUpScreenFertilizer(info: MutableState<Boolean>) {
    Log.d("TAG", "PopUpScreenFertilizer: ")
    Popup(
        alignment = Alignment.TopEnd,
        onDismissRequest = { info.value = false },
        offset = _root_ide_package_.androidx.compose.ui.unit.IntOffset(x=-100,y=150)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(.7f).fillMaxHeight(.5f).clip(RoundedCornerShape(10.dp))
                .background(Color.White).padding(10.dp)
        ){
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "NPK Values in Fertilizers:",
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "• Nitrogen (N): Promotes leaf and stem growth. Essential for chlorophyll production.",

                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "• Phosphorus (P): Encourages root development and reproductive growth.",

                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "• Potassium (K): Improves overall plant health, resistance, and quality.",

                    modifier = Modifier.padding(bottom = 8.dp)
                )
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
fun AutoFertilizer(show: MutableState<Boolean>, result: MutableState<FertilizerRecommendModel?>) {
    var n = remember { mutableStateOf("") }
    var p = remember { mutableStateOf("") }
    var k = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Spacer(Modifier.height(10.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        OutlinedTextField(
            value = n.value,
            onValueChange = { newValue ->
                if (newValue.matches(Regex("^\\d*(\\.\\d*)?\$")) && n.value.length<3) {
                    n.value = newValue
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(10.dp)),
            label = {Text("Nitrogen")},
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

        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = p.value,
            onValueChange = { newValue ->
                if (newValue.matches(Regex("^\\d*(\\.\\d*)?\$")) && p.value.length<3) {
                    p.value = newValue
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(10.dp)),
            label = {Text("Phosphorus")},
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
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = k.value,
            onValueChange = { newValue ->
                if (newValue.matches(Regex("^\\d*(\\.\\d*)?\$")) && k.value.length<3) {
                    k.value = newValue
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(10.dp)),
            label = {Text("Potassium")},
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
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = "Warning: The accuracy of fertilizer recommendations may vary based on available data and local variations.",
            fontSize = 15.sp, modifier = Modifier.height(50.dp), color = Color.Red)
        Spacer(Modifier.height(30.dp))
    }
    Row {
        Button(
            onClick = {
                scope.launch{
                    if (n.value==""||p.value==""||k.value==""){
                        SnackBar.s.showSnackbar("All fields are mandatory")
                        return@launch
                    }
                    val req=PredictionFerReq(
                        n.value.toDouble(),
                        p.value.toDouble(),
                        k.value.toDouble()
                    )
                    val res=  withContext(Dispatchers.IO){
                        getFertilizer(req)
                    }
                    result.value=res.recommend
                    if (result.value!=null){
                        withContext(Dispatchers.IO){
                            DataBaseObject.INSTANCE?.fertilizerDao()?.save(
                                FertilizerSave().apply {
                                    username = userDetail.username
                                    timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                                        Locale.getDefault()).apply {
                                        timeZone = TimeZone.getTimeZone("UTC")
                                    }.format(Date())
                                    input = req
                                    this.result = result.value
                                }
                            )
                        }
                    }
                    show.value=true
                }
            }
        ) {
            Text("get")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPageFer(show: MutableState<Boolean>, result: MutableState<FertilizerRecommendModel?>) {
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
                Text("unable to Predict")
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .verticalScroll(state = rememberScrollState(), enabled = true)
                ) {
                    Row(
                        modifier = Modifier.height(60.dp).fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color(0x884C8325)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Found It...!!")
                        Icon(painter = painterResource(R.drawable.translate_svgrepo_com), contentDescription = ""
                            , modifier = Modifier.height(30.dp).clickable{
                                translate.value= ! translate.value
                            }
                        )
                    }
                    Text("Name", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(result.value?.name?:"")
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("Scientific Name", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(result.value?.scientificName?:"")
                    }
                    Text("NPK Composition", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(result.value?.NPK_Composition?:"")
                    }
                    Text("Application Rate", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(result.value?.applicationRate?:"")
                    }
                    Text("Best Time To Apply", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(result.value?.bestTimeToApply?:"")
                    }
                    Text("Storage and Handling", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(result.value?.storageAndHandling?:"")
                    }
                    Text("Cost Estimate", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(result.value?.costEstimate?:"")
                    }
                    Text("Organic V/S Synthetic", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(result.value?.organicVsSynthetic?:"")
                    }
                    Text("Tips", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Column(
                        modifier = Modifier.padding( start = 20.dp, top = 20.dp),
                    ){
                        GetPoints(
                            if(translate.value)
                                result.value?.tips?.kannada
                            else
                                result.value?.tips?.english
                        )
                    }
                    Text("Recommended Crops", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Column(
                        modifier = Modifier.padding( start = 20.dp, top = 20.dp),
                    ){
                        GetPoints(
                            result.value?.recommendedCrops
                        )
                    }
                    Text("Potential Risks", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Column(
                        modifier = Modifier.padding( start = 20.dp, top = 20.dp),
                    ){
                        GetPoints(
                            result.value?.potentialRisks
                        )
                    }
                    Text("Benefits", fontSize = 18.sp, color = Color(0xBA313131))
                    Spacer(
                        Modifier
                            .fillMaxWidth(.7f)
                            .height(1.dp)
                            .border(color = Color.Black, width = 1.dp))
                    Column(
                        modifier = Modifier.padding( start = 20.dp, top = 20.dp),
                    ){
                        GetPoints(
                            result.value?.benefits
                        )
                    }
                }
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

