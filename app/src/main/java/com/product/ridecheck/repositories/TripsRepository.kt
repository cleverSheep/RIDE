package com.product.ridecheck.repositories

import androidx.lifecycle.MutableLiveData
import com.product.ridecheck.TripPosted
import com.product.ridecheck.TripsArray
import com.product.ridecheck.network.APIClient
import com.product.ridecheck.network.TripsAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripsRepository {
    private val _tripsPostSuccess = MutableLiveData<TripPosted?>()
    private val _scheduledTrips = MutableLiveData<TripsArray?>()

    val tripsPostSuccess = _tripsPostSuccess
    val tripsScheduledTrips = _scheduledTrips

    private val retrofit = APIClient.getClient().create(TripsAPI::class.java)

    fun postScheduledTrips(user: JSONObject) {
        var requestBody: RequestBody? = null
        try {
            requestBody = JSONObject(user.toString()).toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        retrofit.postScheduledTrips(requestBody!!).enqueue(
            object : Callback<TripPosted> {
                override fun onResponse(call: Call<TripPosted>?, response: Response<TripPosted>?) {
                    val routePostSuccess = response?.body()
                    _tripsPostSuccess.postValue(routePostSuccess)
                }

                override fun onFailure(call: Call<TripPosted>?, t: Throwable?) {
                    _tripsPostSuccess.postValue(null)
                }
            }
        )
    }

    fun getScheduledTrips(user: JSONObject) {
        var requestBody: RequestBody? = null
        try {
            requestBody = JSONObject(user.toString()).toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        retrofit.getScheduledTrips(requestBody!!).enqueue(
            object : Callback<TripsArray> {
                override fun onResponse(
                    call: Call<TripsArray>?,
                    response: Response<TripsArray>?
                ) {
                    val trips = response!!.body()
                    _scheduledTrips.postValue(trips)
                }

                override fun onFailure(call: Call<TripsArray>?, t: Throwable?) {
                    _scheduledTrips.postValue(null)
                }

            }
        )
    }
}