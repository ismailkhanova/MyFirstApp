package com.example.myfirstapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfirstapp.adapters.WeatherModel

//class MainViewModel: ViewModel() {
//    val liveDataCurrent = MutableLiveData<WeatherModel>()
//    val liveDataList = MutableLiveData<List<WeatherModel>>()
//
//}

class MainViewModel : ViewModel() {
    val liveDataCurrent = MutableLiveData<WeatherModel>()
    val liveDataList = MutableLiveData<List<WeatherModel>>()
    val liveDataCarWashRecommendation = MutableLiveData<String>()

    fun generateCarWashRecommendation(weatherList: List<WeatherModel>) {
        val suitableDays = weatherList.filter { it.isCarWashRecommended() }
        liveDataCarWashRecommendation.value = when {
            suitableDays.isEmpty() -> "В ближайшие дни нет подходящих условий для мойки автомобиля."
            suitableDays[0] == weatherList[0] -> "Сегодня хорошая погода для мойки автомобиля."
            else -> "Рекомендуется помыть автомобиль ${suitableDays[0].time}."
        }
    }
}