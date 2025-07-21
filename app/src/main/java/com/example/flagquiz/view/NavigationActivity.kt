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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        setContent {
            NavigationBody(auth)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBody(auth: FirebaseAuth) {
    data class BottomNavItem(val label: String, val icon: ImageVector)

    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home),
        BottomNavItem("Learn Flags", Icons.Filled.Info),
        BottomNavItem("Settings", Icons.Filled.Settings)
    )

    var selectedIndex by remember { mutableStateOf(0) }

    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFFFCC99)
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(item.icon, contentDescription = item.label)
                        },
                        label = { Text(item.label) },
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
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
            Image(
                painter = painterResource(id = R.drawable.flags),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFCC99).copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (selectedIndex) {
                    0 -> HomeScreen()
                    1 -> LearnFlagScreen(viewModel)
                    2 -> SettingsScreen(onSignOut = {
                        auth.signOut()
                        val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                        (context as? ComponentActivity)?.finish()
                    })
                }
            }
        }
    }
}