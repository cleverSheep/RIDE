package com.product.ridecheck

import java.io.Serializable

// Response after fetching login auth code
data class UserAuth(val authorization: String)

/**
 * Response after fetching for scheduled trips
 */
data class TripsArray(
    val trips: List<TripResponse>
)

data class TripResponse(
    val sampleId: Int,
    val tripNumber: String,
    val tripDate: String,
    val startTime: String,
    val scheduleOrder: Int,
    val routeName: String,
    val routeDirection: String,
    val vehicleType: String,
    val doorLocation: String,
    val stops: List<TripStop>?
)

data class TripStop(val routeStop: Int, val stopName: String, val stopId: Int)

// Response after posting scheduled trips
data class TripPosted(val success: Boolean)

// Form data for each trip stop
data class TripStopForm(
    var stopName: String = "",
    var stopId: Int = 0,
    var stopLat: Float = 0f,
    var stopLon: Float = 0f,
    val busNumberIndex: Int = 0,
    val busNumber: Int = 0,
    val destination: String = "",
    val arrivalTime: String = "",
    val alighting: Int = 0,
    val boarded: Int = 0,
    val departureTime: String = "",
    val usedWheelchairRamp: Boolean = false,
    val commonActivityIndex: Int = 0,
    val commonActivity: String = "",
    val comments: String = ""
) : Serializable