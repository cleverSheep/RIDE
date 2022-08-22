package com.product.ridecheck

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var scheduledTrips: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scheduledTrips = findViewById(R.id.trips)

        val tripone = TripList(this)
        tripone.bindTripData("2/22", "1",
            listOf(
                TripStop("route 13 leaves in a bit!") {
                    it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
                },
                TripStop("route 13 leaves in a bit!") {
                    it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
                },
                TripStop("route 13 leaves in a bit!") {
                    it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
                },
                TripStop("route 13 leaves in a bit!") {
                    it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
                },
                TripStop("route 13 leaves in a bit!") {
                    it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
                },
                TripStop("route 13 leaves in a bit!") {
                    it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
                },
                TripStop("route 13 leaves in a bit!") {
                    it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
                },
                TripStop("route 13 leaves in a bit!") {
                    it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
                },
                TripStop("route 13 leaves in a bit!") {
                    it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
                }
            ))

        val triptwo = TripList(this)
        triptwo.bindTripData("3/33", "2")

        val tripthree = TripList(this)
        tripthree.bindTripData("4/44", "3", listOf(TripStop("route 14 leaves in a bit!") {
            it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
        }))

        val tripfour = TripList(this)
        tripfour.bindTripData("2/22", "1", listOf(TripStop("route 13 leaves in a bit!") {
            it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
        }))

        val tripfive = TripList(this)
        tripfive.bindTripData("3/33", "2")

        val tripsix = TripList(this)
        tripsix.bindTripData("4/44", "3", listOf(TripStop("route 14 leaves in a bit!") {
            it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
        }))

        val tripseven = TripList(this)
        tripseven.bindTripData("2/22", "1", listOf(TripStop("route 13 leaves in a bit!") {
            it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
        }))

        val tripeight = TripList(this)
        tripeight.bindTripData("3/33", "2")

        val tripnine = TripList(this)
        tripnine.bindTripData("4/44", "3", listOf(TripStop("route 14 leaves in a bit!") {
            it?.context?.startActivity(Intent(it.context, TabbedActivity::class.java))
        }))

        scheduledTrips.addView(tripone)
        scheduledTrips.addView(triptwo)
        scheduledTrips.addView(tripthree)
        scheduledTrips.addView(tripfour)
        scheduledTrips.addView(tripfive)
        scheduledTrips.addView(tripsix)
        scheduledTrips.addView(tripseven)
        scheduledTrips.addView(tripeight)
        scheduledTrips.addView(tripnine)
    }
}