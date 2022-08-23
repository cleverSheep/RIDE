package com.product.ridecheck.tabbed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.product.ridecheck.R

class TripStopFragment : Fragment() {
    private lateinit var busNo: TextInputEditText
    private lateinit var arrivalTime: TextInputEditText
    private lateinit var alighting: TextInputEditText
    private lateinit var boarded: TextInputEditText
    private lateinit var departureTime: TextInputEditText
    private lateinit var comments: TextInputEditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.trip_list_stop_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        busNo = view.findViewById(R.id.bus_no)
        arrivalTime = view.findViewById(R.id.arrival_time)
        alighting = view.findViewById(R.id.alighting_no)
        boarded = view.findViewById(R.id.boarded_no)
        departureTime = view.findViewById(R.id.departure_time)
        comments = view.findViewById(R.id.comments_route)
        if (arguments != null) {
            val bus_no = requireArguments().getInt("busNo")
            val arrival_time = requireArguments().getString("arrivalTime")
            val alightings = requireArguments().getInt("alighting")
            val boarding = requireArguments().getInt("boarded")
            val departure_time = requireArguments().getString("departureTime")
            val comment = requireArguments().getString("comments")

            busNo.setText(bus_no.toString())
            arrivalTime.setText(arrival_time)
            alighting.setText(alightings.toString())
            boarded.setText(boarding.toString())
            departureTime.setText(departure_time)
            comments.setText(comment)
        }
    }

}