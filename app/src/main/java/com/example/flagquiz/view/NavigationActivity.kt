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
import androidx.compose.material.icons.filled.Info
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flagquiz.pages.HomeScreen
import com.example.flagquiz.pages.LearnFlagScreen
import com.example.flagquiz.pages.SettingsScreen
import com.example.flagquiz.viewmodel.MainViewModel
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

    // Use the vector asset for the Learn Flags icon
    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home),
        BottomNavItem("Learn Flags", Icons.Filled.Info), // Use the vector icon from the material icons
        BottomNavItem("Settings", Icons.Filled.Settings)  // Use the vector icon from the material icons
    )

    var selectedIndex by remember { mutableStateOf(0) }

    val viewModel: MainViewModel = viewModel() // Initialize the viewModel here

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFFFCC99)  // Set the bottom bar color to the desired color
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            if (index == 1) {  // Learn Flags screen
                                Icon(item.icon, contentDescription = "Learn Flags")
                            } else {
                                Icon(item.icon, contentDescription = item.label)
                            }
                        },
                        label = { Text(item.label) },
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index  // Update the selected index when an item is clicked
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
                    1 -> LearnFlagScreen(viewModel) // Learn Flag Screen
                    2 -> SettingsScreen() // Settings screen
                }
            }
        }
    }
}