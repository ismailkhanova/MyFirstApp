package com.car.myfirstapp.data

//data class WeatherModel(
//    val city: String,
//    val time: String,
//    val condition: String,
//    val currentTemp: String,
//    val maxTemp: String,
//    val minTemp: String,
//    val imageUrl: String,
//    val hours: String,
//    val chance_of_rain: String,
//    val chance_of_snow: String,
//
//
//
//)

data class WeatherModel(
    val city: String,
    val time: String,
    val condition: String,
    val currentTemp: String,
    val maxTemp: String,
    val minTemp: String,
    val imageUrl: String,
    val hours: String,
    val chance_of_rain: String,
    val chance_of_snow: String,
) {
    fun isCarWashRecommended(): Boolean {
        val chanceOfRain = chance_of_rain.toIntOrNull() ?: 0
        val chanceOfSnow = chance_of_snow.toIntOrNull() ?: 0
        val minTempInt = minTemp.toIntOrNull() ?: 0
        return minTempInt > 0 && chanceOfRain < 40 && chanceOfSnow < 40
    }
}