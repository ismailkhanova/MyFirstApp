package com.example.myfirstapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfirstapp.data.WeatherModel

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
        val suitableDays = weatherList.filterIndexed { index, day ->
            day.isCarWashRecommended() && (index until index + 2).all {
                weatherList.getOrNull(it)?.isCarWashRecommended() ?: false
            }
        }

        liveDataCarWashRecommendation.value = when {
            suitableDays.isEmpty() -> "В ближайшие дни нет подходящих условий для мойки автомобиля."
            suitableDays[0] == weatherList[0] -> "Сегодня хорошая погода для мойки автомобиля, так как осадков нет 2 дня подряд!."
            else -> "Рекомендуется помыть автомобиль ${suitableDays[0].time}."
        }
    }
}
