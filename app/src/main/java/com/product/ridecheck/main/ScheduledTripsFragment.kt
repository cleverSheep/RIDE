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
import android.widget.TextView
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
    private lateinit var noTripsAssigned: TextView

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
        noTripsAssigned = view.findViewById(R.id.no_trips_assigned)
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
                noTripsAssigned.visibility = View.VISIBLE
                listOfTrips.visibility = View.GONE
                return
            } else {
                noTripsAssigned.visibility = View.GONE
                listOfTrips.visibility = View.VISIBLE
                trips.trips.forEach { tripResponse ->
                    bindScheduledTrips(tripResponse, tripResponse.stops, false)
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
                    noTripsAssigned.visibility = View.VISIBLE
                    listOfTrips.visibility = View.GONE
                    return@observe
                }
                if (tripsArray.trips.isEmpty()) {
                    noTripsAssigned.visibility = View.VISIBLE
                    listOfTrips.visibility = View.GONE
                    return@observe
                } else {
                    noTripsAssigned.visibility = View.GONE
                    listOfTrips.visibility = View.VISIBLE
                    tripsArray.trips.forEach { tripResponse ->
                        bindScheduledTrips(tripResponse, tripResponse.stops, false)
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
                    Toast.makeText(context, "Trip data has been submitted!", Toast.LENGTH_SHORT)
                        .show()
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
                trip.put("vehicleNumber", Utils.TRIP_VEHICLE_NUMBER[tripId.toInt()])
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

    private fun bindScheduledTrips(
        tripResponse: TripResponse,
        tripStops: List<TripStop>?,
        tripsNull: Boolean
    ) {
        val tripDate = tripResponse.tripDate.split(" ")[0]
        val intent = Intent(activity, TabbedActivity::class.java)
        val trip_id = tripResponse.sampleId
        intent.putExtra("trip_id", "$trip_id")
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
                ),
                onEnterTripData = { activity?.startActivity(intent) },
                onEnterVehicleNumber = { vehicleNumber ->
                    if (!tripsNull) {
                        Utils.TRIP_VEHICLE_NUMBER[trip_id] = vehicleNumber
                    }

                }
            )
        }
        /**
         * TODO: Replace hardcoded vehicle numbers with data fetched from an api
         */
        scheduledTrips.bindTripData(
            date = tripDate,
            numTrips = tripResponse.stops?.size.toString(),
            stopsEntry = stops!![0],
            arrayOf(
                "1215",
                "1316",
                "1417",
                "1518",
                "1619",
                "2213",
                "2217",
                "2219"
            )
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