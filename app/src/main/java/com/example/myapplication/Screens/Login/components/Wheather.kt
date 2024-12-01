package com.example.myapplication.Screens.Login.components

import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.GlobalStates
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Wheather(){
    val data = GlobalStates.globalStates.weatherData.value
    LaunchedEffect(Unit) {
        if (data!=null) return@LaunchedEffect
        try {
            GlobalStates.globalStates.weatherData.value= withContext(Dispatchers.IO){
                Retrofit.api.getWeatherSoilData()
            }
            Log.d("TAG", "Wheather: ${GlobalStates.globalStates.weatherData.value}")
        }catch (e: Exception){
            Log.d("TAG", "Wheather: ${e.message}")
        }
    }
    when(data){
        null->{
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text("Loading...")
            }
        }
        else -> {
            Column(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = formatDate(),
                    modifier = Modifier
                        .weight(.25f)
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                Row (
                    Modifier
                        .weight(.75f)
                        .fillMaxWidth()
                ){
                    Column (Modifier.weight(.7f)){
                        Text("Temp: ${data.weather?.current?.temperature_2m}${data.weather?.current_units?.temperature_2m}")
                        Text("Wind: ${data.weather?.current?.wind_speed_10m}${data.weather?.current_units?.wind_speed_10m}")
                        Text("Wind Direction: ${data.weather?.current?.wind_direction_10m}${data.weather?.current_units?.wind_direction_10m}")
                        Text("Temp: ${data.weather?.current?.relative_humidity_2m}${data.weather?.current_units?.relative_humidity_2m}")
                        Text("Temp: ${data.weather?.current?.rain}${data.weather?.current_units?.rain}")
                    }
                    Column(
                        modifier = Modifier.weight(.3f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(getWeatherDescription(data.weather?.current?.weather_code))
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionFun(permision: PermissionState) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Button(onClick = {
            permision.launchPermissionRequest()
        }) {
            Text(text = "Grant")
        }
    }
}

suspend fun getWheather(lat:Double,long:Double){
    Log.d("TAG", "getWheather: ${lat} ${long}")
}


fun formatDate(): String {
    val dateTimeString = getCurrentDateTimeString()
    // Define the input date format
    if (dateTimeString=="null") return ""
    val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Parse the input string into LocalDateTime
    val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)

    // Define the output format
    val outputFormatter = DateTimeFormatter.ofPattern("dd, MMMM")

    // Format the dateTime to the desired format
    return dateTime.format(outputFormatter)
}

fun getCurrentDateTimeString(): String {
    val currentDateTime = LocalDateTime.now() // Get the current date and time
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME // ISO format
    return currentDateTime.format(formatter) // Format to string
}


fun getWeatherDescription(weatherCode: Int?): String {
    return when (weatherCode) {
        0 -> "Clear sky"
        1 -> "Mainly clear"
        2 -> "Partly cloudy"
        3 -> "Overcast"
        45, 48 -> "Foggy"
        51, 53, 55 -> "Drizzle"
        61, 63, 65 -> "Rainy"
        71, 73, 75 -> "Snowy"
        95 -> "Thunderstorm"
        in 96..99 -> "Thunderstorm with hail"
        else -> "Unknown weather"
    }
}

@Composable
fun GetSoilDetails(){
    val data = GlobalStates.globalStates.weatherData.value
    when(data){
        null->{
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text("Loading...")
            }
        }
        else -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Soil in you region",
                    modifier = Modifier
                        .weight(.25f)
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(.7f)
                    ) {
                        Text("Nitrogen: ${data.npk?.n}")
                        Spacer(Modifier.height(20.dp))
                        Text("Phosphorus: ${data.npk?.p}")
                        Spacer(Modifier.height(20.dp))
                        Text("Potassium: ${data.npk?.k}")
                        Spacer(Modifier.height(20.dp))

                    }
                    Column(
                        modifier = Modifier.weight(.3f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Ph level")
                        PhLevelChart(data.npk?.ph)
                        Text(String.format("%.1f",data.npk?.ph))
                    }
                }
            }
        }
    }
}

@Composable
fun PhLevelChart(
    phValue: Double?, // Current pH value, e.g., 7.0
    minPh: Float = 0f, // Minimum pH level
    maxPh: Float = 14f, // Maximum pH level
) {
    if (phValue==null) return;
    val sweepAngle = (phValue.div(maxPh)).times(360f)

    Canvas(
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp)
    ) {
        // Draw the background circle
        drawArc(
            color = Color.LightGray,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 20f, cap = StrokeCap.Round)
        )

        // Draw the progress arc
        drawArc(
            color = when {
                phValue < 7 -> Color.Blue // Acidic
                phValue.toFloat() == 7f -> Color.Green // Neutral
                else -> Color.Red // Basic
            },
            startAngle = -90f,
            sweepAngle = sweepAngle.toFloat(),
            useCenter = false,
            style = Stroke(width = 20f, cap = StrokeCap.Round)
        )
    }
}
