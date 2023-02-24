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
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myfirstapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
                    // Create a LatLng object from the location
                    val userLocation = LatLng(location.latitude, location.longitude)


                    // Add a marker at the user's location
                    googleMap.addMarker(
                        MarkerOptions().position(userLocation).title("Your Location")
                    )

                    // Move the camera to the user's location
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))

                    val markerLocation = LatLng(42.838914, 75.297205)
                    googleMap.addMarker(
                        MarkerOptions().position(markerLocation).title("CARWASH"))

                    val placesClient = Places.createClient(requireContext())
                    val request = FindCurrentPlaceRequest.newInstance(
                        listOf(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.TYPES)
                    )
                    placesClient.findCurrentPlace(request).addOnSuccessListener { response: FindCurrentPlaceResponse ->
                        // Filter the response to include only car washes
                        val carWashTypes = listOf(Place.Type.UNIVERSITY)
                        val carWashes = response.placeLikelihoods.filter { it.place.types.intersect(carWashTypes).isNotEmpty() }

                        // Sort the car washes by distance from the user's location
                        val sortedCarWashes = carWashes.sortedBy { placeLikelihood ->
                            val carWashLocation = placeLikelihood.place.latLng
                            if (carWashLocation != null) {
                                val carWashLocationAsLocation = Location("").apply {
                                    latitude = carWashLocation.latitude
                                    longitude = carWashLocation.longitude
                                }
                                location.distanceTo(carWashLocationAsLocation)
                            } else {
                                Float.MAX_VALUE
                            }
                        }

                        Log.d("MapsFragment", "Found ${sortedCarWashes.size} car washes")



                        // Add markers for the 3 closest car washes
                        sortedCarWashes.take(3).forEach { placeLikelihood ->
                            val carWash = placeLikelihood.place
                            val carWashLocation = carWash.latLng
                            if (carWashLocation != null) {
                                val marker = googleMap.addMarker(MarkerOptions().position(carWashLocation).title(carWash.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                                Log.d("MapsFragment", "Adding marker for car wash at $carWashLocation")
                                marker?.tag = carWash
                            }
                        }
                    }

                }
            }
        } else {
            // Request location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}