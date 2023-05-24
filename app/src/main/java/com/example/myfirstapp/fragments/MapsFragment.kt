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
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.maps.android.PolyUtil
import okhttp3.*
import java.io.IOException
import java.util.*



class MapsFragment : Fragment(), OnMapReadyCallback, CarWashInfoFragment.OnNavigateListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var carWashInfoFragment: CarWashInfoFragment
    private var currentPolyline: Polyline? = null


    private val apiKey = "AIzaSyAfktwnw6kIIU_7D8lO86HtnSthDjPk6oQ"

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

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
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

    @SuppressLint("MissingPermission")
    override fun onNavigate(destination: LatLng) {
        // Get the user's current location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)
                getDirections(userLocation, destination)
            }
        }
    }

    override fun onCloseInfo() {
        currentPolyline?.remove() // Remove the polyline when info fragment is closed
    }

    private fun getDirections(origin: LatLng, destination: LatLng) {
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        val str_dest = "destination=" + destination.latitude + "," + destination.longitude
        val parameters = "$str_origin&$str_dest&sensor=false&mode=driving&key=$apiKey"
        val url = "https://maps.googleapis.com/maps/api/directions/json?$parameters"

        val httpClient = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MapsFragment", "Error getting directions: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    if (responseBody != null) {
                        val gson = Gson()
                        val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                        val routes = jsonObject.getAsJsonArray("routes")

                        if (routes.size() > 0) {
                            val route = routes.get(0).asJsonObject
                            val polyline = route.get("overview_polyline").asJsonObject
                            val points = polyline.get("points").asString

                            activity?.runOnUiThread {
                                val decodedPath = PolyUtil.decode(points)
                                googleMap.addPolyline(PolylineOptions().addAll(decodedPath))
                            }
                        }
                    }
                } else {
                    Log.e("MapsFragment", "Error getting directions: ${response.code}")
                }
            }
        })
    }



    private fun fetchNearbyPlaces(location: LatLng, placeType: String) {
        val httpClient = OkHttpClient()
        val urlString =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=${location.latitude},${location.longitude}&radius=5000&type=$placeType&key=$apiKey&language=ru"

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

                                val rating = result.getAsJsonObject().get("rating")?.asFloat

                                val place = PlaceResult(
                                    PlaceResult.Geometry(PlaceResult.Geometry.Location(lat, lng)),
                                    name,
                                    address,
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
