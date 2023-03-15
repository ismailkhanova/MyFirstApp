package com.example.myfirstapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myfirstapp.R
import com.example.myfirstapp.adapters.OpeningHours
import com.example.myfirstapp.adapters.PlaceResult
import kotlinx.android.synthetic.main.fragment_car_wash_info.*

class CarWashInfoFragment : Fragment() {

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
            car_wash_hours.text = place.openingHours.toString()
            car_wash_rating.text = place.rating.toString()
        }
    }

    private fun formatOpeningHours(openingHours: OpeningHours?): String {
        if (openingHours == null) {
            return "N/A"
        }

        val sb = StringBuilder()
        openingHours.weekdayText?.forEach { dayInfo ->
            sb.append(dayInfo).append("\n")
        }
        return sb.toString()
    }


    companion object {
        private const val ARG_PLACE = "arg_place"

        fun newInstance(place: PlaceResult): CarWashInfoFragment {
            val args = Bundle()
            args.putParcelable(ARG_PLACE, place)

            val fragment = CarWashInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

