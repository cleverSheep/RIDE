package com.product.ridecheck.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.product.ridecheck.TripPosted
import com.product.ridecheck.TripsArray
import com.product.ridecheck.repositories.TripsRepository
import org.json.JSONObject

class TripsViewModel(application: Application) : AndroidViewModel(application) {
    private var tripsRepository: TripsRepository = TripsRepository()
    private var _tripsPostSuccess: LiveData<TripPosted?> = tripsRepository.tripsPostSuccess
    private var _tripsScheduledTrips: LiveData<TripsArray?> = tripsRepository.tripsScheduledTrips

    val tripsPostSuccess = _tripsPostSuccess
    val tripsScheduledTrips = _tripsScheduledTrips

    fun postScheduledTrips(
        authorization: String,
        command: String,
        data: JSONObject
    ) {
        val tripRequestJSON = JSONObject()
        tripRequestJSON.put("authorization", authorization)
        tripRequestJSON.put("command", command)
        tripRequestJSON.put("data", data)
        postScheduledTrips(tripRequestJSON)
    }

    private fun postScheduledTrips(user: JSONObject) {
        tripsRepository.postScheduledTrips(user)
    }

    fun getScheduledTrips(
        authorization: String,
        command: String,
        data: JSONObject
    ) {
        val tripRequestJSON = JSONObject()
        tripRequestJSON.put("authorization", authorization)
        tripRequestJSON.put("command", command)
        tripRequestJSON.put("data", data)
        getScheduledTrips(tripRequestJSON)
    }

    private fun getScheduledTrips(user: JSONObject) {
        tripsRepository.getScheduledTrips(user)
    }
}