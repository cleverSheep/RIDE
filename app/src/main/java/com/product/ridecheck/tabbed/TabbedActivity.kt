package com.product.ridecheck.tabbed

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.product.ridecheck.R
import com.product.ridecheck.TripStop
import com.product.ridecheck.TripStopForm
import com.product.ridecheck.Utils
import com.product.ridecheck.Utils.Companion.toNumericVersion
import com.product.ridecheck.main.MainActivity
import com.product.ridecheck.viewmodels.TripsViewModel

class TabbedActivity : FragmentActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ScreenSlidePagerAdapter

    private lateinit var spinner: Spinner
    private lateinit var nextButton: MaterialButton
    private lateinit var previousButton: MaterialButton
    private lateinit var homeButton: MaterialButton
    private lateinit var doneButton: MaterialButton
    private lateinit var footerText: TextView

    private lateinit var tripId: String
    private var tripStopSize = 0
    private lateinit var tripStops: List<TripStop>

    private lateinit var tripsViewModel: TripsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)

        tripId = intent.getStringExtra("trip_id") as String
        tripStopSize = intent.getIntExtra("trip_stop_size", 0)
        tripStops = Utils.TRIP_STOPS[tripId.toInt()]!!

        spinner = findViewById(R.id.spinner)
        val stops = tripStops.map { it.stopName }
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stops)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                saveStopData(viewPager.currentItem)
                viewPager.currentItem = position
                footerText.text = "${viewPager.currentItem + 1}/$tripStopSize"
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        footerText = findViewById(R.id.footer_text)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
        // The pager adapter, which provides the pages to the view pager widget.
        pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        tripsViewModel = ViewModelProvider(this)[TripsViewModel::class.java]

        nextButton = findViewById(R.id.next_page)
        previousButton = findViewById(R.id.previous_page)
        homeButton = findViewById(R.id.home_button)
        doneButton = findViewById(R.id.submit_trips)
        setClickListener()
    }

    private fun setClickListener() {
        nextButton.setOnClickListener {
            if (viewPager.currentItem != pagerAdapter.itemCount - 1) {
                saveStopData(viewPager.currentItem)
                viewPager.currentItem = viewPager.currentItem + 1
                footerText.text = "${viewPager.currentItem + 1}/$tripStopSize"
            } else {
                Toast.makeText(this, "Last page on screen!", Toast.LENGTH_LONG).show()
            }
            if (viewPager.currentItem == pagerAdapter.itemCount - 2) {
                doneButton.visibility = View.VISIBLE
                nextButton.visibility = View.GONE
                footerText.text = "${viewPager.currentItem + 1}/$tripStopSize"
            } else {
                doneButton.visibility = View.GONE
                nextButton.visibility = View.VISIBLE
                footerText.text = "${viewPager.currentItem + 1}/$tripStopSize"
            }
        }
        previousButton.setOnClickListener {
            if (viewPager.currentItem != 0) {
                saveStopData(viewPager.currentItem)
                viewPager.currentItem = viewPager.currentItem - 1
                footerText.text = "${viewPager.currentItem + 1}/$tripStopSize"
            } else {
                Toast.makeText(this, "First page on screen!", Toast.LENGTH_LONG).show()
            }
            if (viewPager.currentItem == pagerAdapter.itemCount - 2) {
                doneButton.visibility = View.VISIBLE
                nextButton.visibility = View.GONE
                footerText.text = "${viewPager.currentItem + 1}/$tripStopSize"
            } else {
                doneButton.visibility = View.GONE
                nextButton.visibility = View.VISIBLE
                footerText.text = "${viewPager.currentItem + 1}/$tripStopSize"
            }
        }
        homeButton.setOnClickListener {
            saveStopData(viewPager.currentItem)
            startActivity(Intent(this, MainActivity::class.java))
        }
        doneButton.setOnClickListener {
            saveStopData(viewPager.currentItem)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun saveStopData(currentItem: Int) {
        val fragment = supportFragmentManager.findFragmentByTag("f$currentItem")
        val view = fragment!!.view
        val busNo = view!!.findViewById(R.id.bus_no) as TextInputEditText
        val arrivalTime = view.findViewById(R.id.arrival_time) as TextInputEditText
        val alighting = view.findViewById(R.id.alighting_no) as TextInputEditText
        val boarded = view.findViewById(R.id.boarded_no) as TextInputEditText
        val departureTime = view.findViewById(R.id.departure_time) as TextInputEditText
        val comments = view.findViewById(R.id.comments_route) as TextInputEditText
        val tripStopForm = TripStopForm(
            Utils.STOP_FORM_DATA["$tripId-$currentItem"]?.stopName ?: "",
            busNumber = busNo.text?.toNumericVersion() ?: 0,
            arrivalTime = arrivalTime.text.toString(),
            alighting = alighting.text?.toNumericVersion() ?: 0,
            boarded = boarded.text?.toNumericVersion() ?: 0,
            departureTime = departureTime.text.toString(),
            comments = comments.text.toString()
        )
        Utils.STOP_FORM_DATA["$tripId-$currentItem"] = tripStopForm
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            saveStopData(viewPager.currentItem)
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
            saveStopData(viewPager.currentItem)
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = tripStopSize
        override fun createFragment(position: Int): Fragment {
            val fragment = TripStopFragment()
            val bundle = Bundle()
            bundle.putString("stopName", Utils.STOP_FORM_DATA["$tripId-$position"]?.stopName!!)
            bundle.putInt("busNo", Utils.STOP_FORM_DATA["$tripId-$position"]?.busNumber!!)
            bundle.putString(
                "arrivalTime",
                Utils.STOP_FORM_DATA["$tripId-$position"]?.arrivalTime!!
            )
            bundle.putInt("alighting", Utils.STOP_FORM_DATA["$tripId-$position"]?.alighting!!)
            bundle.putInt("boarded", Utils.STOP_FORM_DATA["$tripId-$position"]?.boarded!!)
            bundle.putString(
                "departureTime",
                Utils.STOP_FORM_DATA["$tripId-$position"]?.departureTime!!
            )
            bundle.putString("comments", Utils.STOP_FORM_DATA["$tripId-$position"]?.comments!!)
            fragment.arguments = bundle
            return fragment
        }
    }
}