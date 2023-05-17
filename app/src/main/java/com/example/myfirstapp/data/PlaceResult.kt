package com.example.myfirstapp.adapters
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceResult(
    val geometry: Geometry,
    val name: String,
    val address: String?,
    val openingHours: OpeningHours?,
    val rating: Float?
) : Parcelable {
    @Parcelize
    data class Geometry(
        val location: Location
    ) : Parcelable {
        @Parcelize
        data class Location(
            val lat: Double,
            val lng: Double
        ) : Parcelable
    }
}

@Parcelize
data class OpeningHours(
    val openNow: Boolean?,
    val periods: List<Period>?,
    val weekdayText: List<String>?
) : Parcelable

@Parcelize
data class Period(
    val open: Time,
    val close: Time?
) : Parcelable

@Parcelize
data class Time(
    val day: Int,
    val time: String
) : Parcelable
