package com.car.myfirstapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.car.myfirstapp.adapters.PlaceResult
import com.car.myfirstapp.R
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_car_wash_info.*

class CarWashInfoFragment : Fragment() {

    // Define a listener to communicate with parent fragment/activity
    interface OnNavigateListener {
        fun onNavigate(destination: LatLng)
        fun onCloseInfo()
    }

    private var navigateListener: OnNavigateListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_wash_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val place = arguments?.getParcelable<PlaceResult>(ARG_PLACE)

        if (place != null) {
            car_wash_name.text = place.name
            car_wash_address.text = place.address
            car_wash_rating.text = place.rating.toString()

            // Set a click listener for the navigation button
            navigation_button.setOnClickListener {
                navigateListener?.onNavigate(
                    LatLng(place.geometry.location.lat, place.geometry.location.lng)
                )
            }

            // Set a click listener for the close button
            close_button.setOnClickListener {
                navigateListener?.onCloseInfo()
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is OnNavigateListener) {
            navigateListener = parentFragment
        } else {
            throw RuntimeException("$parentFragment must implement OnNavigateListener")
        }
    }

    

    override fun onDetach() {
        super.onDetach()

        navigateListener = null
    }

    companion object {
        private const val ARG_PLACE = "place"

        @JvmStatic
        fun newInstance(place: PlaceResult) =
            CarWashInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PLACE, place)
                }
            }
    }
}
