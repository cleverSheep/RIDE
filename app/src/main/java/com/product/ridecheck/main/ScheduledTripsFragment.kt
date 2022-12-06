package com.product.ridecheck.main

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.product.ridecheck.*
import com.product.ridecheck.tabbed.TabbedActivity
import com.product.ridecheck.viewmodels.TripsViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.net.InetAddress
import java.net.UnknownHostException

class ScheduledTripsFragment : Fragment() {
    private lateinit var listOfTrips: LinearLayout
    private lateinit var scroll: NestedScrollView
    private lateinit var tripsViewModel: TripsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
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
        getScheduledTrips(Utils.TRIPS_ARRAY)

    }

    private fun getScheduledTrips(trips: TripsArray?) {
        if (trips != null) {
            if (trips.trips.isEmpty()) {
                return
            } else {
                trips.trips.forEach { tripResponse ->
                    bindScheduledTrips(tripResponse, tripResponse.stops)
                    if (Utils.STOP_FORM_DATA.isEmpty()) {
                        tripResponse.stops?.forEach { stop ->
                            val key = "${tripResponse.sampleId}-${stop.routeStop}"
                            Utils.STOP_FORM_DATA[key] =
                                TripStopForm(
                                    stopName = stop.stopName,
                                    stopId = stop.stopId
                                )
                        }
                    } else {
                        tripResponse.stops?.forEach { stop ->
                            val key = "${tripResponse.sampleId}-${stop.routeStop}"
                            if (!editedStopData(Utils.STOP_FORM_DATA[key])) {
                                Utils.STOP_FORM_DATA[key] =
                                    TripStopForm(
                                        stopName = stop.stopName,
                                        stopId = stop.stopId
                                    )
                            }
                        }
                    }
                }
                val submitButton = TripListViewFooter(activity as Context)
                listOfTrips.addView(submitButton)
                submitButton.setOnClickListener {
                    postTrips()
                }
            }
        } else {
            tripsViewModel.tripsScheduledTrips.observe(viewLifecycleOwner) { tripsArray ->
                Utils.TRIPS_ARRAY = tripsArray
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
                                Utils.STOP_FORM_DATA[key] =
                                    TripStopForm(
                                        stopName = stop.stopName,
                                        stopId = stop.stopId
                                    )
                            }
                        } else {
                            tripResponse.stops?.forEach { stop ->
                                val key = "${tripResponse.sampleId}-${stop.routeStop}"
                                if (!editedStopData(Utils.STOP_FORM_DATA[key])) {
                                    Utils.STOP_FORM_DATA[key] =
                                        TripStopForm(
                                            stopName = stop.stopName,
                                            stopId = stop.stopId
                                        )
                                }
                            }
                        }
                    }
                }
                val submitButton = TripListViewFooter(activity as Context)
                listOfTrips.addView(submitButton)
                submitButton.setOnClickListener {
                    postTrips()
                }
            }
        }
    }

    private fun postTrips() {
        if (!isNetworkAvailable(activity as Context) || !isInternetAvailable()) {
            Toast.makeText(
                activity,
                "Uh oh! An internet connection is required to submit the trips.",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val tripStops = Utils.STOP_FORM_DATA.toSortedMap(compareByDescending { it })
        var trip = JSONObject()
        var stops = JSONArray()
        val trips = JSONArray()
        val data = JSONObject()
        var tripId = ""
        tripStops.forEach { (key, tripStop) ->
            if (key.split("-")[0] != tripId) {
                if (tripId != "") {
                    trip.put("stops", stops)
                    trips.put(trip)
                    trip = JSONObject()
                    stops = JSONArray()
                }
                tripId = key.split("-")[0]
                trip.put("tripid", tripId)
                trip.put("vehicleNumber", tripStop.busNumber)
            }
            val stop = JSONObject()
            val stopNumber = key.split("-")[1]
            stop.put("routeStop", stopNumber)
            stop.put("stopId", tripStop.stopId)
            stop.put("stopLat", tripStop.stopLat)
            stop.put("stopLon", tripStop.stopLon)
            stop.put("arrived", tripStop.arrivalTime)
            stop.put("departed", tripStop.departureTime)
            stop.put("boardings", tripStop.boarded)
            stop.put("alightings", tripStop.alighting)
            stop.put("comments", tripStop.comments)
            stops.put(stop)
        }
        trip.put("stops", stops)
        trips.put(trip)
        data.put("trips", trips)
        tripsViewModel.postScheduledTrips(
            authorization = Utils.AUTH_CODE,
            command = "ridecheck.savetrips",
            data = data
        )
    }

    private fun bindScheduledTrips(tripResponse: TripResponse, tripStops: List<TripStop>?) {
        val tripDate = tripResponse.tripDate.split(" ")[0]
        val intent = Intent(activity, TabbedActivity::class.java)
        intent.putExtra("trip_id", "${tripResponse.sampleId}")
        intent.putExtra("trip_stop_size", tripResponse.stops?.size)

        Utils.TRIP_STOPS[tripResponse.sampleId] = tripStops ?: emptyList()
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
            stopsEntry = stops!![0]
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

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }

    private fun isInternetAvailable(): Boolean {
        try {
            val address: InetAddress = InetAddress.getByName("www.google.com")
            return !address.equals("")
        } catch (e: UnknownHostException) {
            // Log error
        }
        return false
    }
}