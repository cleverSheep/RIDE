package com.product.ridecheck.tabbed

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.product.ridecheck.R
import java.util.*

class TripStopFragment : Fragment() {
    private lateinit var stopName: TextView
    private lateinit var busNo: TextInputEditText
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
        busNo = view.findViewById(R.id.bus_no)
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
        comments = view.findViewById(R.id.comments_route)
        if (arguments != null) {
            val stop_name = requireArguments().getString("stopName")
            val bus_no = requireArguments().getInt("busNo")
            val arrival_time = requireArguments().getString("arrivalTime")
            val alightings = requireArguments().getInt("alighting")
            val boarding = requireArguments().getInt("boarded")
            val departure_time = requireArguments().getString("departureTime")
            val comment = requireArguments().getString("comments")

            stopName.text = stop_name
            busNo.setText(bus_no.toString())
            arrivalTime.setText(arrival_time)

            alighting.setText(alightings.toString())
            alightingNo = alightings

            boarded.setText(boarding.toString())
            boardedNo = boarding

            departureTime.setText(departure_time)
            comments.setText(comment)
        }
        bindUI()
    }

    private fun bindUI() {
        arrivalTimePicker.setOnClickListener {
            // Get Current Time
            val c: Calendar = Calendar.getInstance()
            val mHour = c.get(Calendar.HOUR_OF_DAY)
            val mMinute = c.get(Calendar.MINUTE)

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                activity,
                { _, hourOfDay, minute ->
                    val hour: Int
                    val amOrPm: String
                    if (hourOfDay > 12) {
                        hour = hourOfDay - 12
                        amOrPm = "PM"
                    } else {
                        hour = hourOfDay
                        amOrPm = "AM"
                    }
                    arrivalTime.setText("$hour:$minute $amOrPm")
                },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }

        departureTimePicker.setOnClickListener {
            // Get Current Time
            val c: Calendar = Calendar.getInstance()
            val mHour = c.get(Calendar.HOUR_OF_DAY)
            val mMinute = c.get(Calendar.MINUTE)

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                activity,
                { _, hourOfDay, minute ->
                    val hour: Int
                    val amOrPm: String
                    if (hourOfDay > 12) {
                        hour = hourOfDay - 12
                        amOrPm = "PM"
                    } else {
                        hour = hourOfDay
                        amOrPm = "AM"
                    }
                    departureTime.setText("$hour:$minute $amOrPm")
                },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }

        alightingAdd.setOnClickListener {
            alightingNo += 1
            alighting.setText(alightingNo.toString())
        }

        alightingRemove.setOnClickListener {
            if (alightingNo == 0) {
                return@setOnClickListener
            } else {
                alightingNo -= 1
                alighting.setText(alightingNo.toString())

            }
        }

        boardedRemove.setOnClickListener {
            if (boardedNo == 0) {
                return@setOnClickListener
            } else {
                boardedNo -= 1
                boarded.setText(boardedNo.toString())
            }
        }

        boardedAdd.setOnClickListener {
            boardedNo += 1
            boarded.setText(boardedNo.toString())
        }

    }

}