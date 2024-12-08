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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.DTO.CropRecReq
import com.example.myapplication.DTO.CropRecommendationModel
import com.example.myapplication.DTO.FertilizerRecommendModel
import com.example.myapplication.DTO.PredictionFerReq
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

        ShowFertilizerRecommendation(result.result,result.input)
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
    val showKannada = remember { mutableStateOf(false) }
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showResults.value=false
            scope.launch{
                sheetState.hide()
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F8E9)) // Light green background
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF81C784)) // Green header
                    .padding(16.dp)
                    .clip(RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Found",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = if (showKannada.value) "ಕನ್ನಡ" else "English",
                        fontSize = 16.sp,
                        color = Color(0xFF627362),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(8.dp))
                    Switch(
                        checked = showKannada.value,
                        onCheckedChange = { showKannada.value = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF81C784),
                            uncheckedThumbColor = Color(0xFF81C784),
                            checkedTrackColor = Color(0xFF388E3C),
                            uncheckedTrackColor = Color(0xFFC8E6C9)
                        )
                    )
                }
            }

            Section("Name",if(showKannada.value) result.result?.kannadaName else result.result?.className)
            Section("Description",if(showKannada.value) result.result?.kannadaDescription else result.result?.description)
            Section("Cause",if(showKannada.value) result.result?.kannadaCause else result.result?.cause)
            var list=result.result?.recommendedActions?.map {
                 if(showKannada.value) it.kannadaAction else it.action
            } ?: listOf()
            ListSection("Recommended Actions",list)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCrop(result: RecommendationSave, showResults: MutableState<Boolean>){
    val sheetState= rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope= rememberCoroutineScope()

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
        ShowCropRecommendation(result.result,result.input)
    }
}


fun <T : Any> generatePropertyList(instance: T): List<String> {
    return instance::class.members
        .filterIsInstance<kotlin.reflect.KProperty1<T, *>>() // Filter for properties
        .map { property ->
            "${property.name} : ${property.get(instance)}"
        }
}


@Composable
fun ShowFertilizerRecommendation(fertilizer: FertilizerRecommendModel?, input: PredictionFerReq?){
    var showKannadaTips = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9)) // Light green background
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF81C784)) // Green header
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = fertilizer?.name ?: "Crop Details",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(Modifier.height(16.dp))
        if(input == null){
            Section("Input","Auto")
        }
        else{
            ShowDynamicProperties(input)
        }
        fertilizer?.scientificName?.let { Section("Scientific Name", it) }
        fertilizer?.NPK_Composition?.let { Section("NPK Composition", it) }
        fertilizer?.applicationRate?.let { Section("Application Rate", it) }
        fertilizer?.bestTimeToApply?.let { Section("Best Time to Apply", it) }
        fertilizer?.storageAndHandling?.let { Section("Storage & Handling", it) }
        fertilizer?.costEstimate?.let { Section("Cost Estimate", it) }
        fertilizer?.organicVsSynthetic?.let { Section("Organic vs Synthetic", it) }

        fertilizer?.recommendedCrops?.let { ListSection("Recommended Crops", it) }
        fertilizer?.benefits?.let { ListSection("Benefits", it) }
        fertilizer?.potentialRisks?.let { ListSection("Potential Risks", it) }
        Text(
            text = "Cultivation Tips",
            fontSize = 18.sp,
            color = Color(0xFF388E3C), // Dark green header
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        // Toggle Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = if (showKannadaTips.value) "ಕನ್ನಡ" else "English",
                fontSize = 16.sp,
                color = Color(0xFF627362),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = showKannadaTips.value,
                onCheckedChange = { showKannadaTips.value = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF81C784),
                    uncheckedThumbColor = Color(0xFF81C784),
                    checkedTrackColor = Color(0xFF388E3C),
                    uncheckedTrackColor = Color(0xFFC8E6C9)
                )
            )
        }
        // Tips Content
        fertilizer?.tips?.let { tips ->
            val tipList = if (showKannadaTips.value) tips.kannada else tips.english
            tipList?.forEach { tip ->
                TipItem(tip)
            }
        }
    }
}

@Composable
fun ListSection(title: String, items: List<String>) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = Color(0xFF388E3C),
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(12.dp)
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = item,
                    fontSize = 16.sp,
                    color = Color(0xFF313131)
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
@Composable
fun ShowCropRecommendation(crop: CropRecommendationModel?, input: CropRecReq?) {
    val showKannada = remember { mutableStateOf(false) } // Toggle state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9)) // Light green background
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Title Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF81C784)) // Green header
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = crop?.name ?: "Crop Details",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(Modifier.height(16.dp))


        Column {
            if(input == null){
                Section("Input","Auto")
            }
            else{
                ShowDynamicProperties(input)
            }
        }


        // Sections
        Section("Name in Kannada", crop?.kannadaName)
        Section("Scientific Name", crop?.scientificName)
        Section("Climate Requirements", crop?.climateRequirements)
        Section("Soil Type", crop?.soilType)
        Section("Watering Needs", crop?.wateringNeeds)
        Section("Growing Season", crop?.growingSeason)

        // Tips Section with Toggle
        Text(
            text = "Cultivation Tips",
            fontSize = 18.sp,
            color = Color(0xFF388E3C), // Dark green header
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        // Toggle Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = if (showKannada.value) "ಕನ್ನಡ" else "English",
                fontSize = 16.sp,
                color = Color(0xFF627362),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = showKannada.value,
                onCheckedChange = { showKannada.value = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF81C784),
                    uncheckedThumbColor = Color(0xFF81C784),
                    checkedTrackColor = Color(0xFF388E3C),
                    uncheckedTrackColor = Color(0xFFC8E6C9)
                )
            )
        }

        // Tips Content
        crop?.tips?.let { tips ->
            val tipList = if (showKannada.value) tips.kannada else tips.english
            tipList.forEach { tip ->
                TipItem(tip)
            }
        }
    }
}

@Composable
fun Section(title: String, content: String?) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = Color(0xFF388E3C), // Dark green header
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(12.dp)

        ) {
            Text(
                text = content ?: "N/A",
                fontSize = 16.sp,
                color = Color(0xFF313131) // Neutral dark text
            )
        }
    }
}

@Composable
fun <T : Any> ShowDynamicProperties(input: T?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F8E9)) // Light green background
            .padding(16.dp)
    ) {
        if (input == null) {
            Section("Input", "Auto")
        } else {
            // Render properties dynamically
            input::class.members
                .filterIsInstance<kotlin.reflect.KProperty1<T, *>>() // Filter for properties
                .forEach { property ->
                    DynamicPropertyItem(property.name, property.get(input))
                }
        }
    }
}

@Composable
fun TipItem(tip: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp)) // Light green tip background
            .padding(12.dp) // Green border
            .padding(8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = "• $tip",
            fontSize = 14.sp,
            color = Color(0xFF313131) // Neutral dark text
        )
        Spacer(Modifier.height(5.dp))
    }
}

@Composable
fun DynamicPropertyItem(name: String, value: Any?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF81C784), RoundedCornerShape(8.dp)) // Green border
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$name: ",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF388E3C), // Dark green
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value?.toString() ?: "N/A",
            fontSize = 16.sp,
            color = Color(0xFF313131),
            modifier = Modifier.weight(2f)
        )
    }
}
