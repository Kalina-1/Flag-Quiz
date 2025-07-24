
package com.example.flagquiz.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.flagquiz.R

// Simple data class to hold country info: name, capital, and flag image resource
data class FlagAndCapital(val countryName: String, val capital: String, val flagResourceId: Int)

class MainViewModel : ViewModel() {
    // A list of countries with their flags and capitals arranged alphabetically by country name
    val flagsAndCapitals = mutableStateOf(
        listOf(
            FlagAndCapital("Argentina", "Buenos Aires", R.drawable.argentina),
            FlagAndCapital("Australia", "Canberra", R.drawable.australia),
            FlagAndCapital("Brazil", "Brasília", R.drawable.brazil),
            FlagAndCapital("Canada", "Ottawa", R.drawable.canada),
            FlagAndCapital("China", "Beijing", R.drawable.china),
            FlagAndCapital("Colombia", "Bogotá", R.drawable.colombia),
            FlagAndCapital("Egypt", "Cairo", R.drawable.egypt),
            FlagAndCapital("Finland", "Helsinki", R.drawable.finland),
            FlagAndCapital("France", "Paris", R.drawable.france),
            FlagAndCapital("Germany", "Berlin", R.drawable.germany),
            FlagAndCapital("India", "New Delhi", R.drawable.india),
            FlagAndCapital("Indonesia", "Jakarta", R.drawable.indonesia),
            FlagAndCapital("Iraq", "Baghdad", R.drawable.iraq),
            FlagAndCapital("Italy", "Rome", R.drawable.italy),
            FlagAndCapital("Japan", "Tokyo", R.drawable.japan),
            FlagAndCapital("Kenya", "Nairobi", R.drawable.kenya),
            FlagAndCapital("Malaysia", "Kuala Lumpur", R.drawable.malaysia),
            FlagAndCapital("Mexico", "Mexico City", R.drawable.mexico),
            FlagAndCapital("Nigeria", "Abuja", R.drawable.nigeria),
            FlagAndCapital("Norway", "Oslo", R.drawable.norway),
            FlagAndCapital("Poland", "Warsaw", R.drawable.poland),
            FlagAndCapital("Saudi Arabia", "Riyadh", R.drawable.saudi_arabia),
            FlagAndCapital("South Africa", "Pretoria", R.drawable.south_africa),
            FlagAndCapital("South Korea", "Seoul", R.drawable.south_korea),
            FlagAndCapital("Spain", "Madrid", R.drawable.spain),
            FlagAndCapital("Sweden", "Stockholm", R.drawable.sweden),
            FlagAndCapital("Switzerland", "Bern", R.drawable.switzerland),
            FlagAndCapital("Turkey", "Ankara", R.drawable.turkey),
            FlagAndCapital("UK", "London", R.drawable.uk),
            FlagAndCapital("USA", "Washington, D.C.", R.drawable.usa),
            FlagAndCapital("Venezuela", "Caracas", R.drawable.venezuela)
        )
    )
}
