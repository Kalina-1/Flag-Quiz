
package com.example.flagquiz.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagquiz.viewmodel.MainViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.flagquiz.viewmodel.FlagAndCapital

@Composable
fun LearnFlagScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Learn Flags and Capitals", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        // Display each country's flag, name, and capital
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.flagsAndCapitals.value) { flagAndCapital ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Image(
                        painter = painterResource(id = flagAndCapital.flagResourceId),
                        contentDescription = "Flag of ${flagAndCapital.countryName}",
                        modifier = Modifier.size(100.dp) // Adjust size as needed
                    )
                    Text(text = flagAndCapital.countryName, fontSize = 20.sp)
                    Text(text = "Capital: ${flagAndCapital.capital}", fontSize = 18.sp)
                }
            }
        }
    }
}
