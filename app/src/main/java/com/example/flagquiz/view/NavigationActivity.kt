package com.example.flagquiz.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagquiz.pages.HighScoresScreen
import com.example.flagquiz.pages.HomeScreen
import com.example.flagquiz.pages.SettingsScreen
import com.example.flagquiz.R

class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBody() {
    // Define bottom navigation items
    data class BottomNavItem(val label: String, val icon: ImageVector)

    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home),
//        BottomNavItem("Quiz", Icons.Filled.Quiz),
//        BottomNavItem("High Scores", Icons.Filled.Scores),
        BottomNavItem("Settings", Icons.Filled.Settings)  // Ensure 'Settings' is included
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFFFCC99)  // Set the bottom bar color to the desired color
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index // Update the selected index when an item is clicked
                        }
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF97B57),
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                title = { Text("Flag Quiz App") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.flags),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Apply content with semi-transparent background
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFCC99).copy(alpha = 0.8f)) // Semi-transparent background
                    .padding(horizontal = 16.dp), // Add padding around content
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Switch screens based on selected index
                when (selectedIndex) {
                    0 -> HomeScreen() // Dashboard or home screen
//                    1 -> QuizScreen() // Current quiz screen
//                    2 -> HighScoresScreen() // High scores screen
                    1 -> SettingsScreen() // Settings screen
                }
            }
        }
    }
}