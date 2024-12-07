package com.example.myapplication.Screens.Login.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.Screens.Login.components.GetSoilDetails
import com.example.myapplication.Screens.Login.components.Wheather
import com.example.myapplication.Screens.Login.components.formatDate
import com.example.myapplication.singleton.GlobalStates
import kotlinx.coroutines.launch

@Composable
fun DrawerInit(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomDrawerContent(
                onMenuItemClicked = {
                    scope.launch { drawerState.close() }
                },
                onLogOut = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }
    ) {
        HomepageMainScreen(navController, drawerState)
    }
}

@Composable
fun HomepageMainScreen(navController: NavController, drawerState: DrawerState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7FDF9)) // Subtle off-white background with a greenish tint
    ) {
        // Top Bar
        Box(
            modifier = Modifier
                .weight(0.08f)
                .background(Color(0xFF388E3C)) // Dark green for top bar
        ) {
            TopView(drawerState)
        }

        // Main Content Area
        Box(
            modifier = Modifier.weight(0.92f)
        ) {
            Content(navController)
        }
    }
}

@Composable
fun Content(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Weather Section
        SectionCard(
            title = formatDate(),
            content = { Wheather() },
            backgroundColor = Color(0xFFE8F5E9) // Soft greenish-white for content cards
        )

        Spacer(Modifier.height(16.dp))

        // Soil Details Section
        SectionCard(
            title = "Soil Details",
            content = { GetSoilDetails() },
            backgroundColor = Color(0xFFE8F5E9)
        )

        Spacer(Modifier.height(24.dp))

        // Recent Detections Section
        RecentDetections()

        Spacer(Modifier.height(24.dp))

        // What We Offer Section
        SectionCard(
            title = "What We Offer",
            content = {
                OfferDetails(
                    listOf(
                        "Disease Detection" to "Upload plant images to diagnose diseases.",
                        "Crop Recommendation" to "Input soil and weather data for crop and fertilizer suggestions.",
                        "Real-Time Weather" to "Current weather conditions and wind data.",
                        "Soil Data Highlights" to "Show NPK values and pH with manual input or estimation options.",
                        "Recent Activity" to "View detection results and recommendations."
                    )
                )
            },
            backgroundColor = Color(0xFFE8F5E9)
        )
    }

    FloatingButton(navController)
}

@Composable
fun SectionCard(title: String, content: @Composable () -> Unit, backgroundColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32), // Dark green text
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun OfferDetails(details: List<Pair<String, String>>) {
    details.forEach { (title, description) ->
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF388E3C), // Medium green for headings
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = description,
            fontSize = 14.sp,
            color = Color(0xFF6D6D6D), // Neutral gray for descriptions
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun RecentDetections() {
    Text(
        text = "Recent Activities",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF388E3C), // Medium green for title
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )

    GetService(
        painter = painterResource(R.drawable.file_send_svgrepo_com),
        text = "Recent Detections",
        points = listOf("View recently detected plant diseases."),
        onNav = {
            GlobalStates.globalStates.navController?.navigate("recent/1")
        }
    )

    GetService(
        painter = painterResource(R.drawable.hand_holding_seedling_svgrepo_com),
        text = "Recent Recommendations",
        points = listOf("View recommended crops and fertilizers."),
        onNav = {
            GlobalStates.globalStates.navController?.navigate("recent/2")
        }
    )

    GetService(
        painter = painterResource(R.drawable.fertilizer_svgrepo_com),
        text = "Recent Fertilizers",
        points = listOf("Check recently suggested fertilizers."),
        onNav = {
            GlobalStates.globalStates.navController?.navigate("recent/3")
        }
    )
}

@Composable
fun GetService(painter: Painter, text: String, points: List<String>, onNav: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF1F8E9)) // Very light green for service cards
            .clickable { onNav() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(0.2f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = Color(0xFF388E3C) // Medium green for icons
            )
        }
        Column(modifier = Modifier.weight(0.6f)) {
            points.forEach {
                Text(
                    text = it,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    color = Color(0xFF6D6D6D), // Neutral gray for text
                    textAlign = TextAlign.Start
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight()
                .background(Color(0xFF43A047)), // Dark green button
            contentAlignment = Alignment.Center
        ) {
            Text("Go", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
    Spacer(Modifier.height(10.dp))
}
