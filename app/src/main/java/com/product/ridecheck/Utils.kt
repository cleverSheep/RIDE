package com.product.ridecheck

import android.text.Editable

class Utils {
    companion object {
        var AUTH_CODE = ""
        /**
         * Map the "trip_id-stop_position" to TripStopForm
         */
        val STOP_FORM_DATA = HashMap<String, TripStopForm>()

        val TRIP_STOPS = HashMap<Int, List<TripStop>>()

        fun Editable.toNumericVersion(): Int {
            if (this.isEmpty()) {
                return 0
            }
            return this.toString().toInt()
        }
    }
}