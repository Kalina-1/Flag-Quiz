package com.example.flagquiz.view

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flagquiz.pages.HomeScreen
import com.example.flagquiz.pages.LearnFlagScreen
import com.example.flagquiz.pages.SettingsScreen
import com.example.flagquiz.viewmodel.MainViewModel
import com.example.flagquiz.R
import com.google.firebase.auth.FirebaseAuth

class NavigationActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()

        // If user is not logged in, redirect to login screen
        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Prevent going back to this activity
        }

        // Load the main navigation UI
        setContent {
            NavigationBody(auth)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBody(auth: FirebaseAuth) {
    // Structure to hold nav item label and icon
    data class BottomNavItem(val label: String, val icon: ImageVector)

    // Bottom nav bar items
    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home),
        BottomNavItem("Learn Flags", Icons.Filled.Info),
        BottomNavItem("Settings", Icons.Filled.Settings)
    )

    var selectedIndex by remember { mutableStateOf(0) } // Keeps track of selected tab
    val viewModel: MainViewModel = viewModel() // ViewModel for shared state
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFFFCC99) // Light orange background for nav bar
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index } // Switch between screens
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF97B57), // App bar background
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                title = { Text("Flag Quiz App") } // Title of app
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background image of flags
            Image(
                painter = painterResource(id = R.drawable.flags),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Semi-transparent layer over image for readability
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFCC99).copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Render screen based on selected index
                when (selectedIndex) {
                    0 -> HomeScreen()
                    1 -> LearnFlagScreen(viewModel)
                    2 -> SettingsScreen(
                        onSignOut = {
                            // Sign the user out from Firebase
                            auth.signOut()

                            // Clear user data from shared preferences
                            val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.clear()
                            editor.apply()

                            // Navigate back to LoginActivity and clear back stack
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)

                            // Close current activity to prevent back navigation
                            (context as? ComponentActivity)?.finish()
                        }
                    )
                }
            }
        }
    }
}
