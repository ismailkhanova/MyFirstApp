package com.example.myfirstapp

data class DayItem(
    val city: String,
    val time: String,
    val condition: String,
    val imageUrl: String,
    val currentTemp: String,
    val maxTemp: String,
    val minTemp: String,
    val wind_speed: String,
    val chance_of_rain: String,
    val chance_of_snow: String,
    val hours: String
)
