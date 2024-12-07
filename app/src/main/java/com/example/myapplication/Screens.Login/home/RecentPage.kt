package com.example.myapplication.Screens.Login.home

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.DataBase.DiseaseSave
import com.example.myapplication.DataBase.FertilizerSave
import com.example.myapplication.DataBase.RecommendationSave
import com.example.myapplication.R
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.userDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun RecentPage(mode: String?){
    Column(

    ) {
        Row(
            modifier = Modifier.weight(.08f).fillMaxWidth()
                .background(Color(0x6ACBECAF))
                .shadow(elevation = 1.dp, clip = false)
                .padding(start = 10.dp, end = 10.dp),
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
                            popUpTo("chat", { inclusive = true })
                        }
                    },

                ){
                Icon(painter = painterResource(R.drawable.go_back_svgrepo_com), contentDescription = "",
                    modifier = Modifier.scale(.6f)
                )
            }

            Text(text = if (mode=="1") "Recent Detection" else if (mode=="2") "Recent Crops" else "Recent Fertilizers" , fontSize = 20.sp)

        }
        Box(Modifier.weight(.92f).fillMaxWidth().fillMaxHeight()){
            when(mode){
                "1" -> {
                    RecentDetectionPage()
                }
                "2" ->{
                    RecentCropsPage()
                }
                "3" ->{
                    RecentFertilizerPage()
                }
                else -> {
                    Box(
                        Modifier.fillMaxSize()
                    ){
                        Text("Nothing to show")
                    }
                }
            }
        }
    }
}

@Composable
fun RecentFertilizerPage(){
    val items = remember { mutableStateListOf<FertilizerSave>() }
    val showResults = remember { mutableStateOf(false) }
    val curItem = remember { mutableStateOf<FertilizerSave>(FertilizerSave()) }
    LaunchedEffect(Unit) {
        val data = withContext(Dispatchers.IO){
            DataBaseObject.INSTANCE?.fertilizerDao()?.get(userDetail.username)
        }
        if (data != null) {
            items.addAll(data)
        }
    }
    if (showResults.value){
        BottomSheetFer(curItem.value,showResults)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light background for the list
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Space between items
    ) {
        items(items.size) { item ->
            ListItemCardfer(items[item],showResults,curItem)
        }
    }
}



@Composable
fun RecentCropsPage(){
    val items = remember { mutableStateListOf<RecommendationSave>() }
    val showResults = remember { mutableStateOf(false) }
    val curItem = remember { mutableStateOf<RecommendationSave>(RecommendationSave()) }
    LaunchedEffect(Unit) {
        val data = withContext(Dispatchers.IO){
            DataBaseObject.INSTANCE?.recommendationDao()?.get(userDetail.username)
        }
        if (data != null) {
            items.addAll(data)
        }
    }
    if (showResults.value){
        BottomSheetCrop(curItem.value,showResults)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light background for the list
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Space between items
    ) {
        items(items.size) { item ->
            ListItemCardCrop(items[item],showResults,curItem)
        }
    }
}

@Composable
fun RecentDetectionPage(){
    val items = remember { mutableStateListOf<DiseaseSave>() }
    val showResults = remember { mutableStateOf(false) }
    val curItem = remember { mutableStateOf<DiseaseSave>(DiseaseSave()) }
    LaunchedEffect(Unit) {
        val data = withContext(Dispatchers.IO){
            DataBaseObject.INSTANCE?.diseaseDao()?.get(userDetail.username)
        }
        if (data != null) {
            items.addAll(data)
        }
    }
    if (showResults.value){
        BottomSheet(curItem.value,showResults)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light background for the list
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Space between items
    ) {
        items(items.size) { item ->
            ListItemCard(items[item],showResults,curItem)
        }
    }
}

@Composable
fun ListItemCardCrop(
    item: RecommendationSave,
    showResults: MutableState<Boolean>,
    curItem: MutableState<RecommendationSave>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp)
            .clickable{
                curItem.value=item
                showResults.value=true
            }
        ,
        shape = RoundedCornerShape(12.dp),

        ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Optional: Display an image or icon
            Icon(
                painter = painterResource(id = R.drawable.menu_2_svgrepo_com), // Replace with your icon
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFBBDEFB)), // Light blue background
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Text content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.timestamp,
                    color = Color.Black
                )

            }
        }
    }
}

@Composable
fun ListItemCardfer(
    item: FertilizerSave,
    showResults: MutableState<Boolean>,
    curItem: MutableState<FertilizerSave>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp)
            .clickable{
                curItem.value=item
                showResults.value=true
            }
        ,
        shape = RoundedCornerShape(12.dp),

        ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Optional: Display an image or icon
            Icon(
                painter = painterResource(id = R.drawable.menu_2_svgrepo_com), // Replace with your icon
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFBBDEFB)), // Light blue background
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Text content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.timestamp,
                    color = Color.Black
                )

            }
        }
    }
}


@Composable
fun ListItemCard(
    item: DiseaseSave,
    showResults: MutableState<Boolean>,
    curItem: MutableState<DiseaseSave>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp)
            .clickable{
                curItem.value=item
                showResults.value=true
            }
        ,
        shape = RoundedCornerShape(12.dp),

    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Optional: Display an image or icon
            Icon(
                painter = painterResource(id = R.drawable.menu_2_svgrepo_com), // Replace with your icon
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFBBDEFB)), // Light blue background
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Text content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.timestamp,
                    color = Color.Black
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetFer(result: FertilizerSave,showResults: MutableState<Boolean>){
    val sheetState= rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope= rememberCoroutineScope()
    val translate = remember { mutableStateOf(false) }
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showResults.value=false
            scope.launch{
                sheetState.hide()
            }
        },
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(state = rememberScrollState(), enabled = true)
        ) {
            Text("Input")
            Spacer(
                Modifier
                    .fillMaxWidth(.7f)
                    .height(1.dp)
                    .border(color = Color.Black, width = 1.dp))
            Column(
                modifier = Modifier.padding( start = 20.dp, top = 20.dp),
            ){
                GetPoints(
                    if (result.input==null)
                        listOf("Unavailable")
                    else
                        generatePropertyList(result.input!!)
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
                Text(result.result?.name?:"")
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
                Text(result.result?.scientificName?:"")
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
                Text(result.result?.NPK_Composition?:"")
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
                Text(result.result?.applicationRate?:"")
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
                Text(result.result?.bestTimeToApply?:"")
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
                Text(result.result?.storageAndHandling?:"")
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
                Text(result.result?.costEstimate?:"")
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
                Text(result.result?.organicVsSynthetic?:"")
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
                        result.result?.tips?.kannada
                    else
                        result.result?.tips?.english
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
                    result.result?.recommendedCrops
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
                    result.result?.potentialRisks
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
                    result.result?.benefits
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(result: DiseaseSave,showResults: MutableState<Boolean>){
    val sheetState= rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope= rememberCoroutineScope()
    val translate = remember { mutableStateOf(false) }
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showResults.value=false
            scope.launch{
                sheetState.hide()
            }
        },
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(state = rememberScrollState(), enabled = true)
        ) {
            Text("Name", fontSize = 18.sp, color = Color(0xBA313131))
            Spacer(
                Modifier
                    .fillMaxWidth(.7f)
                    .height(1.dp)
                    .border(color = Color.Black, width = 1.dp))
            Row(
                modifier = Modifier
                    .heightIn(min = 50.dp)
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    if(translate.value)
                        result.result?.kannadaName?:""
                    else
                        result.result?.className?:""
                )
            }
            Spacer(Modifier.height(10.dp))
            Text("Description", fontSize = 18.sp, color = Color(0xBA313131))
            Spacer(
                Modifier
                    .fillMaxWidth(.7f)
                    .height(1.dp)
                    .border(color = Color.Black, width = 1.dp))
            Row(
                modifier = Modifier
                    .heightIn(min = 50.dp)
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    if(translate.value)
                        result.result?.kannadaDescription?:""
                    else
                        result.result?.description?:""
                )
            }
            Text("Cause", fontSize = 18.sp, color = Color(0xBA313131))
            Spacer(
                Modifier
                    .fillMaxWidth(.7f)
                    .height(1.dp)
                    .border(color = Color.Black, width = 1.dp))
            Row(
                modifier = Modifier
                    .heightIn(min = 50.dp)
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    if(translate.value)
                        result.result?.kannadaCause?:""
                    else
                        result.result?.cause?:""
                )
            }
            Text("Recommended Actions", fontSize = 18.sp, color = Color(0xBA313131))
            Spacer(
                Modifier
                    .fillMaxWidth(.7f)
                    .height(1.dp)
                    .border(color = Color.Black, width = 1.dp))
            Column(
                modifier = Modifier.padding( start = 20.dp, top = 20.dp),
            ){
                result.result?.recommendedActions?.forEach {
                    Text(
                        if(translate.value)
                            it.kannadaAction
                        else
                            it.action
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCrop(result: RecommendationSave, showResults: MutableState<Boolean>){
    val sheetState= rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope= rememberCoroutineScope()
    val translate = remember { mutableStateOf(false) }
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showResults.value=false
            scope.launch{
                sheetState.hide()
            }
        },
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(state = rememberScrollState(), enabled = true)
        ) {
            Text("Input")
            Spacer(
                Modifier
                    .fillMaxWidth(.7f)
                    .height(1.dp)
                    .border(color = Color.Black, width = 1.dp))
            Column(
                modifier = Modifier.padding( start = 20.dp, top = 20.dp),
            ){
                GetPoints(
                    if(result.input==null){
                        listOf("Auto")
                    }
                    else
                        generatePropertyList(result.input!!)
                )
            }
            Spacer(Modifier.height(10.dp))
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
                Text(
                    if(translate.value)
                        result.result?.kannadaName?:""
                    else
                        result.result?.name?:""
                )
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
                Text(result.result?.scientificName?:"")
            }
            Text("Climate Requirements", fontSize = 18.sp, color = Color(0xBA313131))
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
                Text(result.result?.climateRequirements?:"")
            }
            Text("Soil Type", fontSize = 18.sp, color = Color(0xBA313131))
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
                Text(result.result?.soilType?:"")
            }
            Text("Watering Needs", fontSize = 18.sp, color = Color(0xBA313131))
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
                Text(result.result?.wateringNeeds?:"")
            }
            Text("Growing Season", fontSize = 18.sp, color = Color(0xBA313131))
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
                Text(result.result?.growingSeason?:"")
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
                        result.result?.tips?.kannada
                    else
                        result.result?.tips?.english
                )
            }
        }
    }
}


fun <T : Any> generatePropertyList(instance: T): List<String> {
    return instance::class.members
        .filterIsInstance<kotlin.reflect.KProperty1<T, *>>() // Filter for properties
        .map { property ->
            "${property.name} : ${property.get(instance)}"
        }
}