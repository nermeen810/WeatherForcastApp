package com.example.weatherforcast.view.activity

import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforcast.R
import com.example.weatherforcast.viewModel.MapViewModel
import com.example.weatherforcast.viewModel.WeatherDetailsViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var mapViewModel: MapViewModel
    private lateinit var mMap: GoogleMap
    var userLocationMarker: Marker? = null
    lateinit var latitude: String
    lateinit var longitude: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val sharedPref = application.getSharedPreferences("weather", Context.MODE_PRIVATE)
        latitude = sharedPref.getString("lat", "0").toString()
        longitude = sharedPref.getString("lng", "0").toString()
        mapViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(
            MapViewModel::class.java
        )

        val addFlaot1_btn = this.findViewById(R.id.add_flout_btn) as FloatingActionButton
        addFlaot1_btn.setOnClickListener(View.OnClickListener {
            mapViewModel.setWeatherData(latitude, longitude)
            finish()
        })
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val current = LatLng(latitude?.toDouble()!!, longitude?.toDouble()!!)
        var select = current
        val markerOptions = MarkerOptions()
        markerOptions.position(current)
        markerOptions.title("marker in select")
        userLocationMarker = mMap.addMarker(markerOptions)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(current))

        mMap.setOnMapClickListener { point ->
//            Toast.makeText(
//                this,
//                point.latitude.toString() + ", " + point.longitude,
//                Toast.LENGTH_SHORT
//            ).show()
            latitude = point.latitude.toString()
            longitude = point.longitude.toString()
            select = LatLng(point.latitude, point.longitude)
            userLocationMarker!!.position = select
            userLocationMarker!!.rotation = maxOf(0.5f, 0.5f)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 20f))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(select))
        }
        // Add a marker in Sydney and move the camera


    }


}