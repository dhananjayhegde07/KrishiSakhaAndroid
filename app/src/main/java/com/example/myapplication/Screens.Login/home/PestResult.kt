package com.example.myapplication.Screens.Login.home


import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.DTO.PestRecommend
import com.example.myapplication.R
import kotlinx.coroutines.launch
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPage_pest(
    showResults: MutableState<Boolean>,
    result: MutableState<PestRecommend?>,
    outputFile: File
){
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
        when{
            result.value==null->{
                Unable_pest(listOf(
                    "Image may not be correct",
                    "Check Internet Connection",
                    "Server May be offline",
                ),painterResource(R.drawable.error_inspect_ios11_svgrepo_com))
            }
            else -> {
                ShowRes(result.value,outputFile)
            }
        }

    }
}

@Composable
fun ShowRes(result: PestRecommend?, outputFile: File) {
    val showKannada = remember { mutableStateOf(false) }

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
        Spacer(Modifier.height(10.dp))
        Section("Image preview","")
        val bitmap = remember(outputFile) {
            BitmapFactory.decodeFile(outputFile.absolutePath)?.asImageBitmap()
        }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            if (bitmap==null){
                Text("NO preview available")
            }else{
                Image(bitmap = bitmap, contentDescription = "", modifier = Modifier.height(100.dp))
            }
        }
        Section("Name", result?.insect)

        ListSection("Recommended Actions",result?.pest ?: listOf())
    }

}





@Composable
fun Unable_pest(causes: List<String>,painter: Painter){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the icon
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        // Main error message
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Subtext explaining possible causes
        Text(
            text = "Possible reasons:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Display the list of causes
        causes.forEach { cause ->
            Text(
                text = "- $cause",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

