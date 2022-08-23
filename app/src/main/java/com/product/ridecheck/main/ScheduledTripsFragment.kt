package com.product.ridecheck.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.product.ridecheck.R
import com.product.ridecheck.TripResponse
import com.product.ridecheck.TripStop
import com.product.ridecheck.TripStopForm
import com.product.ridecheck.Utils
import com.product.ridecheck.tabbed.TabbedActivity
import com.product.ridecheck.viewmodels.TripsViewModel
import org.json.JSONObject

class ScheduledTripsFragment : Fragment() {
    private lateinit var listOfTrips: LinearLayout
    private lateinit var scroll: NestedScrollView
    private lateinit var tripsViewModel: TripsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scheduled_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listOfTrips = view.findViewById(R.id.trips)
        scroll = view.findViewById(R.id.nested_scroll)
        tripsViewModel = ViewModelProvider(this)[TripsViewModel::class.java]
        tripsViewModel.getScheduledTrips(
            authorization = Utils.AUTH_CODE,
            command = "ridecheck.getscheduledtrips",
            data = JSONObject().put("timestamp", "")
        )
        getScheduledTrips()
    }

    private fun getScheduledTrips() {
        tripsViewModel.tripsScheduledTrips.observe(viewLifecycleOwner) { tripsArray ->
            if (tripsArray == null) {
                return@observe
            }
            if (tripsArray.trips.isEmpty()) {
                return@observe
            } else {
                tripsArray.trips.forEach { tripResponse ->
                    bindScheduledTrips(tripResponse, tripResponse.stops)
                    if (Utils.STOP_FORM_DATA.isEmpty()) {
                        tripResponse.stops?.forEach { stop ->
                            val key = "${tripResponse.sampleId}-${stop.routeStop}"
                            Utils.STOP_FORM_DATA[key] = TripStopForm(stopId = stop.stopId)
                        }
                    } else {
                        tripResponse.stops?.forEach { stop ->
                            val key = "${tripResponse.sampleId}-${stop.routeStop}"
                            if (!editedStopData(Utils.STOP_FORM_DATA[key])) {
                                Utils.STOP_FORM_DATA[key] = TripStopForm(stopId = stop.stopId)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun bindScheduledTrips(tripResponse: TripResponse, tripStops: List<TripStop>?) {
        val tripDate = tripResponse.tripDate.split(" ")[0]
        val intent = Intent(activity, TabbedActivity::class.java)
        intent.putExtra("trip_id", "${tripResponse.sampleId}")
        intent.putExtra("trip_stop_size", tripResponse.stops?.size)
        val scheduledTrips = TripListView(activity as Context)
        val stops = tripStops?.map {
            TripStopItem(
                title = resources.getString(
                    R.string.trip_stop_card_title,
                    tripResponse.routeName,
                    tripResponse.startTime,
                    tripResponse.doorLocation
                )
            ) { activity?.startActivity(intent) }
        }
        scheduledTrips.bindTripData(
            date = tripDate,
            numTrips = tripResponse.stops?.size.toString(),
            tripStops = stops
        )
        listOfTrips.addView(scheduledTrips)
    }

    private fun editedStopData(routeStop: TripStopForm?): Boolean {
        if (routeStop == null) {
            return false
        }
        routeStop.let {
            if (it.arrivalTime != ""
                || it.departureTime != ""
                || it.alighting != 0
                || it.boarded != 0
                || it.comments != ""
            ) {
                return true
            }
        }
        return false
    }
}