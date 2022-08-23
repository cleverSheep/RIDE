package com.product.ridecheck.network

import com.product.ridecheck.TripPosted
import com.product.ridecheck.TripsArray
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TripsAPI {
    @Headers("Content-Type: text/plain")
    @POST("Process")
    fun postScheduledTrips(@Body requestbody: RequestBody): Call<TripPosted>

    @Headers("Content-Type: text/plain")
    @POST("Process")
    fun getScheduledTrips(@Body requestbody: RequestBody): Call<TripsArray>
}