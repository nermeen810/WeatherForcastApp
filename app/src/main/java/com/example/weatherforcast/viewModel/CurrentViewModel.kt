package com.example.weatherforcast.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.media.audiofx.BassBoost
import android.os.Looper
import android.widget.Toast
import android.provider.Settings;

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforcast.data.WeatherRepository
import com.example.weatherforcast.model.WeatherResponse
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


class CurrentViewModel (application: Application) : AndroidViewModel(application) {
    var repository: WeatherRepository
    var weatherLiveData= MutableLiveData<List<WeatherResponse>>()
    var fusedLocationProviderClient: FusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(application.applicationContext)
    var latitude = 33.4418
    var longitude = -94.0377
    var address: String? = null
    val PERMISSION_ID = 20
    init {
    repository=WeatherRepository(application)
    }

    fun loadWeatherData(): LiveData<WeatherResponse> {

        return repository.fetchData(latitude.toString(),longitude.toString())

    }

    /*@SuppressLint("MissingPermission")
    private  fun getLastLocation(): Unit {
        if (checkPermission()) {
            if (checkLocationEnabled()) {
                fusedLocationProviderClient.lastLocation
                    .addOnCompleteListener(object : OnCompleteListener<Location> {
                        override fun onComplete(task: Task<Location>) {
                            var location: Location = task.getResult()
                            if (location == null) {
                               requestNewLocationData()
                            } else {
                                latitude = location.getLatitude()
                                longitude = location.getLongitude()
                            }
                        }
                    })
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                .startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application.applicationContext)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation

        }
    }

    private fun checkLocationEnabled(): Boolean {
        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermission(): Boolean {
         if (ActivityCompat.checkSelfPermission(
                application.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                application.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
             return true
        }
             return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            application, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>?, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode, permissions,
            grantResults
        )
        if (requestCode == PERMISSION_ID) {
            if (grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                getLastLocation()
            }
        }
    }*/


}