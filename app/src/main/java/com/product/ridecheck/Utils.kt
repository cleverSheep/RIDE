package com.product.ridecheck

import android.text.Editable

class Utils {
    companion object {
        var AUTH_CODE = ""
        /**
         * Map the "trip_id-stop_position" to TripStopForm
         */
        val STOP_FORM_DATA = HashMap<String, TripStopForm>()

        /**
         * Map the "trip_id" to vehicle number
         */
        val TRIP_VEHICLE_NUMBER = HashMap<Int, Int>()

        val STOP_FORM_LIVE_PASSENGERS = HashMap<String, Int>()

        val TRIP_STOPS = HashMap<Int, List<TripStop>>()

        var TRIPS_ARRAY: Route? = null

        fun Editable.toNumericVersion(): Int {
            if (this.isEmpty()) {
                return 0
            }
            return this.toString().toInt()
        }
    }
}