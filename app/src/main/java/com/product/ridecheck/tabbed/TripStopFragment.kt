package com.product.ridecheck.tabbed

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.product.ridecheck.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class TripStopFragment : Fragment() {
    private lateinit var stopName: TextView
    private lateinit var arrivalTime: TextInputEditText
    private lateinit var arrivalTimePicker: ImageButton
    private lateinit var alighting: TextInputEditText
    private lateinit var alightingRemove: ImageButton
    private lateinit var alightingAdd: ImageButton
    private lateinit var boarded: TextInputEditText
    private lateinit var boardedRemove: ImageButton
    private lateinit var boardedAdd: ImageButton
    private lateinit var departureTime: TextInputEditText
    private lateinit var departureTimePicker: ImageButton
    private lateinit var usedWheelchairRamp: CheckBox
    private lateinit var commonActivitySpinner: Spinner
    private lateinit var comments: TextInputEditText

    private var alightingNo = 0
    private var boardedNo = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.trip_list_stop_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stopName = view.findViewById(R.id.trip_stop_name)
        arrivalTime = view.findViewById(R.id.arrival_time)
        arrivalTimePicker = view.findViewById(R.id.arrival_time_picker)
        alighting = view.findViewById(R.id.alighting_no)
        alightingAdd = view.findViewById(R.id.alighting_add)
        alightingRemove = view.findViewById(R.id.alighting_remove)
        boarded = view.findViewById(R.id.boarded_no)
        boardedAdd = view.findViewById(R.id.boarded_add)
        boardedRemove = view.findViewById(R.id.boarded_remove)
        departureTime = view.findViewById(R.id.departure_time)
        departureTimePicker = view.findViewById(R.id.departure_time_picker)
        usedWheelchairRamp = view.findViewById(R.id.ada_checkbox)
        commonActivitySpinner = view.findViewById(R.id.common_activity_spinner)
        comments = view.findViewById(R.id.comments_route)
        if (arguments != null) {
            val stop_name = requireArguments().getString("stopName")
            val arrival_time = requireArguments().getString("arrivalTime")
            val alightings = requireArguments().getInt("alighting")
            val boarding = requireArguments().getInt("boarded")
            val departure_time = requireArguments().getString("departureTime")
            val used_wheelchair_ramp = requireArguments().getBoolean("usesWheelchairRamp")
            val common_activity = requireArguments().getInt("commonActivity")
            val comment = requireArguments().getString("comments")

            stopName.text = stop_name

            arrivalTime.setText(arrival_time)
            alighting.setText(alightings.toString())
            alightingNo = alightings

            boarded.setText(boarding.toString())
            boardedNo = boarding

            departureTime.setText(departure_time)

            usedWheelchairRamp.isChecked = used_wheelchair_ramp

            val commonActivities = arrayOf(
                "Operator Boarding and Alighting",
                "Doorway Conversation with Operator",
                "Assisted Boarding",
                "Assisted Alighting",
                "Stroller/Cart"
            )
            val commonActivityAdapter = ArrayAdapter(activity as Context, android.R.layout.simple_spinner_dropdown_item, commonActivities)
            commonActivitySpinner.adapter = commonActivityAdapter
            commonActivitySpinner.setSelection(common_activity)

            comments.setText(comment)
        }
        bindUI()
    }

    private fun bindUI() {
        arrivalTimePicker.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()

                val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                val time = current.format(formatter)
                arrivalTime.setText(time.toString())
            } else {
                var date = Date()
                val formatter = SimpleDateFormat("HH:mm a")
                val time: String = formatter.format(date)
                arrivalTime.setText(time)
            }
        }

        departureTimePicker.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()

                val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                val time = current.format(formatter)
                departureTime.setText(time.toString())
            } else {
                var date = Date()
                val formatter = SimpleDateFormat("HH:mm a")
                val time: String = formatter.format(date)
                departureTime.setText(time)
            }
        }

        alightingAdd.setOnClickListener {
            alightingNo += 1
            alighting.setText(alightingNo.toString())
            liveCountListener?.invoke(-1)
        }

        alightingRemove.setOnClickListener {
            if (alightingNo == 0) {
                return@setOnClickListener
            } else {
                alightingNo -= 1
                alighting.setText(alightingNo.toString())
                liveCountListener?.invoke(1)

            }
        }

        boardedRemove.setOnClickListener {
            if (boardedNo == 0) {
                return@setOnClickListener
            } else {
                boardedNo -= 1
                boarded.setText(boardedNo.toString())
                liveCountListener?.invoke(-1)
            }
        }

        boardedAdd.setOnClickListener {
            boardedNo += 1
            boarded.setText(boardedNo.toString())
            liveCountListener?.invoke(1)
        }

    }

    companion object {
        var liveCountListener: ((count: Int)->Unit)? = null
    }

}