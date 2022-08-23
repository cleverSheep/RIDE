package com.product.ridecheck.tabbed

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.product.ridecheck.R
import com.product.ridecheck.TripStopForm
import com.product.ridecheck.Utils
import com.product.ridecheck.main.MainActivity
import com.product.ridecheck.viewmodels.TripsViewModel
import org.json.JSONArray
import org.json.JSONObject

class TabbedActivity : FragmentActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ScreenSlidePagerAdapter

    private lateinit var nextButton: MaterialButton
    private lateinit var previousButton: MaterialButton
    private lateinit var homeButton: MaterialButton
    private lateinit var submitTripsButton: MaterialButton

    private lateinit var tripId: String
    private var tripStopSize = 0

    private lateinit var tripsViewModel: TripsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)

        tripId = intent.getStringExtra("trip_id") as String
        tripStopSize = intent.getIntExtra("trip_stop_size", 0)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
        nextButton = findViewById(R.id.next_page)
        previousButton = findViewById(R.id.previous_page)
        homeButton = findViewById(R.id.home_button)
        submitTripsButton = findViewById(R.id.submit_trips)
        setClickListener()

        // The pager adapter, which provides the pages to the view pager widget.
        pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        tripsViewModel = ViewModelProvider(this)[TripsViewModel::class.java]
    }

    private fun setClickListener() {
        nextButton.setOnClickListener {
            if (viewPager.currentItem != pagerAdapter.itemCount - 1) {
                viewPager.currentItem = viewPager.currentItem + 1
                saveStopData(viewPager.currentItem)
            } else {
                Toast.makeText(this, "Last page on screen!", Toast.LENGTH_LONG).show()
            }
        }
        previousButton.setOnClickListener {
            if (viewPager.currentItem != 0) {
                viewPager.currentItem = viewPager.currentItem - 1
                saveStopData(viewPager.currentItem)
            } else {
                Toast.makeText(this, "First page on screen!", Toast.LENGTH_LONG).show()
            }
        }
        homeButton.setOnClickListener {
            saveStopData(viewPager.currentItem)
            startActivity(Intent(this, MainActivity::class.java))
        }
/*        submitTripsButton.setOnClickListener {
            saveStopData(viewPager.currentItem)
            tripsViewModel.postScheduledTrips(
                authorization = Utils.AUTH_CODE,
                command = "ridecheck.savetrips",
                JSONObject()
            )
            startActivity(Intent(this, MainActivity::class.java))
        }*/
    }

    private fun saveStopData(currentItem: Int) {
        val fragment = supportFragmentManager.findFragmentByTag("f$currentItem")
        val view = fragment?.view
        val busNo = view?.findViewById(R.id.bus_no) as TextInputEditText
        val arrivalTime = view.findViewById(R.id.arrival_time) as TextInputEditText
        val alighting = view.findViewById(R.id.alighting_no) as TextInputEditText
        val boarded = view.findViewById(R.id.boarded_no) as TextInputEditText
        val departureTime = view.findViewById(R.id.departure_time) as TextInputEditText
        val comments = view.findViewById(R.id.comments) as TextInputEditText
        val tripStopForm = TripStopForm(
            busNumber = busNo.text.toString().toInt(),
            arrivalTime = arrivalTime.text.toString(),
            alighting = alighting.text.toString().toInt(),
            boarded = boarded.text.toString().toInt(),
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
        private var busNo: TextInputEditText? = null
        private var arrivalTime: TextInputEditText? = null
        private var alighting: TextInputEditText? = null
        private var boarded: TextInputEditText? = null
        private var departureTime: TextInputEditText? = null
        private var comments: TextInputEditText? = null

        override fun createFragment(position: Int): Fragment {
            val fragment = TripStopFragment()
            val view = fragment.view
            busNo = view?.findViewById(R.id.bus_no)
            arrivalTime = view?.findViewById(R.id.arrival_time)
            alighting = view?.findViewById(R.id.alighting_no)
            boarded = view?.findViewById(R.id.boarded_no)
            departureTime = view?.findViewById(R.id.departure_time)
            comments = view?.findViewById(R.id.comments)
            bindData(position)
            return fragment

        }

        fun bindData(position: Int) {
            busNo?.setText(Utils.STOP_FORM_DATA["$tripId-$position"]?.busNumber.toString())
            arrivalTime?.setText(Utils.STOP_FORM_DATA["$tripId-$position"]?.arrivalTime.toString())
            alighting?.setText(Utils.STOP_FORM_DATA["$tripId-$position"]?.alighting.toString())
            boarded?.setText(Utils.STOP_FORM_DATA["$tripId-$position"]?.boarded.toString())
            departureTime?.setText(Utils.STOP_FORM_DATA["$tripId-$position"]?.departureTime.toString())
            comments?.setText(Utils.STOP_FORM_DATA["$tripId-$position"]?.comments.toString())
        }
    }
}