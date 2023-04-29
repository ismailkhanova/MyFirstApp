package com.example.myfirstapp.fragments

//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.example.myfirstapp.R
//
//class MapsFragment : Fragment() {
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_maps, container, false)
//    }
//}
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.myfirstapp.R
import com.example.myfirstapp.adapters.PlaceResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException
import java.util.*


class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var carWashInfoFragment: CarWashInfoFragment

    private val apiKey = "AIzaSyD-eGtdg9wWO0rv0jb2rlDdzB2sP2o1H8s" // Замените на свой API ключ

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission was granted, enable location on the map
                googleMap.isMyLocationEnabled = true
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        carWashInfoFragment = CarWashInfoFragment()

        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Enable location on the map
            googleMap.isMyLocationEnabled = true

            // Get the user's current location
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    // Add a marker at the user's location
                    googleMap.addMarker(
                        MarkerOptions().position(userLocation).title("Your Location")
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                    fetchNearbyPlaces(userLocation, "car_wash")
                }
            }
        } else {
            // Request location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        googleMap.setOnInfoWindowClickListener { marker ->
            val place = marker.tag as? PlaceResult
            if (place != null) {
                showCarWashInfo(place)
            }
        }
    }


    private fun fetchNearbyPlaces(location: LatLng, placeType: String) {
        val httpClient = OkHttpClient()
        val urlString =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=${location.latitude},${location.longitude}&radius=5000&type=$placeType&key=$apiKey"

        val request = Request.Builder()
            .url(urlString)
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MapsFragment", "Error fetching nearby places: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val gson = Gson()
                        val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                        val results = jsonObject.getAsJsonArray("results")

                        activity?.runOnUiThread {
                            results.forEach { result ->
                                val name = result.getAsJsonObject().get("name").asString
                                val geometry = result.getAsJsonObject().get("geometry")
                                val location = geometry.getAsJsonObject().get("location")
                                val lat = location.getAsJsonObject().get("lat").asDouble
                                val lng = location.getAsJsonObject().get("lng").asDouble
                                val address = result.getAsJsonObject().get("vicinity")?.asString

                                val openingHours = null

                                val rating = result.getAsJsonObject().get("rating")?.asFloat

                                val place = PlaceResult(
                                    PlaceResult.Geometry(PlaceResult.Geometry.Location(lat, lng)),
                                    name,
                                    address,
                                    openingHours,
                                    rating
                                )

                                val placeLocation = LatLng(place.geometry.location.lat, place.geometry.location.lng)
                                val marker = googleMap.addMarker(
                                    MarkerOptions().position(placeLocation).title(place.name)
                                        .icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_GREEN
                                            )
                                        )
                                )
                                Log.d(
                                    "MapsFragment",
                                    "Adding marker for ${placeType.lowercase(Locale.ROOT)} at $placeLocation"
                                )
                                marker?.tag = place
                            }
                        }
                    }
                } else {
                    Log.e("MapsFragment", "Error fetching nearby places: ${response.code}")
                }
            }
        })
    }

    private fun showCarWashInfo(place: PlaceResult) {
        val carWashInfoFragment = CarWashInfoFragment.newInstance(place)

        childFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in_up,
                R.anim.slide_out_down,
                R.anim.slide_in_down,
                R.anim.slide_out_up
            )
            add(R.id.info_container, carWashInfoFragment)
            addToBackStack(null)
        }
    }
}