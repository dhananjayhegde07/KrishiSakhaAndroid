package com.example.myapplication.Screens.Login.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.myapplication.DTO.FertilizerRecommendModel
import com.example.myapplication.DTO.PredictionFerReq
import com.example.myapplication.DTO.PredictionFerRes
import com.example.myapplication.R
import com.example.myapplication.SnackBar
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.GlobalStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FertilizerScreen(){
    val auto = remember { mutableStateOf(true) }
    val show = remember { mutableStateOf(false) }
    val result = remember { mutableStateOf<FertilizerRecommendModel?>(null) }
    if (show.value){
        ResultsPageFer(show,result)
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
                modifier = Modifier.fillMaxWidth().height(40.dp)
            ) {
                Text("Fertilizer", fontSize = 20.sp,)
            }
            AutoFertilizer(show,result)
        }
        TopBarFer()
    }

}


@Composable
fun TopBarFer(){
    Row (
        modifier = Modifier.fillMaxWidth().height(50.dp).padding(end = 10.dp, start = 7.dp),
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
fun AutoFertilizer(show: MutableState<Boolean>, result: MutableState<FertilizerRecommendModel?>) {
    var n = remember { mutableStateOf("") }
    var p = remember { mutableStateOf("") }
    var k = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Spacer(Modifier.height(10.dp))

    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp)
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
                    val res=  withContext(Dispatchers.IO){
                        getFertilizer(PredictionFerReq(
                            n.value.toDouble(),
                            p.value.toDouble(),
                            k.value.toDouble()
                        ))
                    }
                    result.value=res.recommend
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



suspend fun getFertilizer(req: PredictionFerReq): PredictionFerRes{
    return try{
        Retrofit.api.predictFertilizer(req)
    }catch (e:Exception){
        PredictionFerRes(null,false)
    }
}

