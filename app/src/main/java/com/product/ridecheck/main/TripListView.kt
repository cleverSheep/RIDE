package com.product.ridecheck.main

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.ViewCompat
import com.google.android.material.button.MaterialButton
import com.product.ridecheck.R

class TripListView(context: Context) : LinearLayout(context) {
    private var tripListHeader = TripListHeader(context)

    init {
        layoutParams =
            Constraints.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL
        setBackgroundResource(R.drawable.trip_list_background)
        elevation = 8f

        val margins = MarginLayoutParams::class.java.cast(layoutParams)
        val margin = 16
        margins.topMargin = margin
        margins.bottomMargin = margin + 16
        margins.leftMargin = margin + 8
        margins.rightMargin = margin + 8
        layoutParams = margins
    }

    fun bindTripData(
        date: String,
        numTrips: String,
        tripStops: List<TripStopItem>? = emptyList()
    ) {
        removeAllViews()
        tripListHeader.setDate(date)
        tripListHeader.setNumberTrips(numTrips)
        addView(tripListHeader)
        tripStops?.forEach { tripStop ->
            addView(TripListStop.create(context, tripStop))
        }
    }
}

class TripListHeader(context: Context) : LinearLayout(context) {
    private var tripsListHeaderDate: TextView
    private var tripsListHeaderNumTrips: TextView

    init {
        LayoutInflater.from(context).inflate(
            R.layout.trip_list_header,
            this,
            true
        )
        tripsListHeaderDate = ViewCompat.requireViewById(this, R.id.trips_list_header_date)
        tripsListHeaderNumTrips =
            ViewCompat.requireViewById(this, R.id.trips_list_header_number_trips)
    }

    fun setDate(date: String = "1/1/22") {
        tripsListHeaderDate.text = date
    }

    fun setNumberTrips(trips: String = "0") {
        tripsListHeaderNumTrips.text = trips
    }
}

class TripListStop(context: Context) : ConstraintLayout(context) {
    private var tripListStopHeader: TextView
    private var tripListStopButton: MaterialButton

    init {
        LayoutInflater.from(context).inflate(
            R.layout.trip_list_stop,
            this,
            true
        )
        tripListStopHeader = ViewCompat.requireViewById(this, R.id.trip_list_stop_header)
        tripListStopButton =
            ViewCompat.requireViewById(this, R.id.trip_list_stop_button)
        tripListStopButton.setOnClickListener {
        }
    }

    companion object {
        fun create(context: Context, data: TripStopItem) = TripListStop(context).apply {
            tripListStopHeader.text = data.title
            tripListStopButton.setOnClickListener {
                data.onClick?.invoke()
            }
        }
    }
}

data class TripStopItem(val title: String, val onClick: (() -> Unit)?)