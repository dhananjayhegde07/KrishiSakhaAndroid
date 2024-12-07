package com.example.myapplication.Screens.Login.components

import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun Wheather() {
    val data = GlobalStates.globalStates.weatherData.value
    LaunchedEffect(Unit) {
        if (data != null) return@LaunchedEffect
        try {
            GlobalStates.globalStates.weatherData.value = withContext(Dispatchers.IO) {
                Retrofit.api.getWeatherSoilData()
            }
            Log.d("TAG", "Wheather: ${GlobalStates.globalStates.weatherData.value}")
        } catch (e: Exception) {
            Log.d("TAG", "Wheather: ${e.message}")
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            ,
        contentAlignment = Alignment.Center
    ) {
        when (data) {
            null -> {
                Text(
                    text = "Loading...",
                    color = Color(0xFF2E7D32), // Dark green
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            else -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            verticalArrangement=Arrangement.spacedBy(8.dp) // Vertical spacing between items
                        ) {
                            WeatherDetailText(
                                label = "Temperature",
                                value = "${data.weather?.current?.temperature_2m}${data.weather?.current_units?.temperature_2m}"
                            )
                            WeatherDetailText(
                                label = "Wind Speed",
                                value = "${data.weather?.current?.wind_speed_10m}${data.weather?.current_units?.wind_speed_10m}"
                            )
                            WeatherDetailText(
                                label = "Wind Direction",
                                value = "${data.weather?.current?.wind_direction_10m}${data.weather?.current_units?.wind_direction_10m}"
                            )
                            WeatherDetailText(
                                label = "Humidity",
                                value = "${data.weather?.current?.relative_humidity_2m}${data.weather?.current_units?.relative_humidity_2m}"
                            )
                            WeatherDetailText(
                                label = "Rain",
                                value = "${data.weather?.current?.rain}${data.weather?.current_units?.rain}"
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = getWeatherDescription(data.weather?.current?.weather_code),
                                color = Color(0xFF388E3C), // Slightly darker green
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WeatherDetailText(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$label:",
            color = Color(0xFF388E3C), // Medium green
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            color = Color(0xFF388E3C), // Medium green
            fontSize = 14.sp
        )
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
