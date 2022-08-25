package com.product.ridecheck.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.button.MaterialButton
import com.product.ridecheck.R

class TripListView : LinearLayout {
    private var tripListHeader: TripListHeader

    constructor(context: Context) : super(context) {
        tripListHeader = TripListHeader(context)
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        tripListHeader = TripListHeader(context)
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        tripListHeader = TripListHeader(context)
        initializeView()
    }

    private fun initializeView() {
        layoutParams =
            Constraints.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
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
        setPadding(0, 0, 0, 16)
    }

    fun bindTripData(
        date: String,
        numTrips: String,
        stopsEntry: TripStopItem
    ) {
        removeAllViews()
        tripListHeader.setDate(date)
        tripListHeader.setNumberTrips("$numTrips stops")
        addView(tripListHeader)
        addView(TripListStop.create(context, stopsEntry))
    }
}

class TripListHeader : LinearLayout {
    private lateinit var tripsListHeaderDate: TextView
    private lateinit var tripsListHeaderNumTrips: TextView

    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializeView()
    }

    private fun initializeView() {
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

class TripListStop : ConstraintLayout {
    private lateinit var tripListStopHeader: TextView
    private lateinit var tripListStopButton: MaterialButton

    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializeView()
    }

    private fun initializeView() {
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

class TripListViewFooter : MaterialButton {
    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializeView()
    }

    private fun initializeView() {
        layoutParams =
            Constraints.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

        val margins = ViewGroup.MarginLayoutParams::class.java.cast(layoutParams)
        val margin = 16
        margins.topMargin = margin
        margins.bottomMargin = margin + 16
        margins.leftMargin = margin + 8
        margins.rightMargin = margin + 8
        layoutParams = margins

        text = "Submit Trips"
        setBackgroundColor(ContextCompat.getColor(context, R.color.dark_blue_primary))
    }
}

data class TripStopItem(val title: String, val onClick: (() -> Unit)?)