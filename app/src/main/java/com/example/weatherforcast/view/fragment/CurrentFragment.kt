package com.example.weatherforcast.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforcast.databinding.FragmentCurrentBinding
import com.example.weatherforcast.model.Hourly
import com.example.weatherforcast.model.WeatherResponse
import com.example.weatherforcast.view.adapter.HourlyAdapter
import com.example.weatherforcast.viewModel.CurrentViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*


class CurrentFragment : Fragment() {
    lateinit var currentViewModel: CurrentViewModel
    lateinit var binding:FragmentCurrentBinding
    val PermissionId=1
    lateinit var  hourlyAdapter: HourlyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            currentViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(
                CurrentViewModel::class.java)
        getLatestLocation()
        currentViewModel.loadWeatherData().observe(this, {
         //   Toast.makeText(context, ""+it,Toast.LENGTH_SHORT).show()
         it?.let{
             setData(it)
         }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hourlyAdapter = HourlyAdapter(arrayListOf())
       initUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCurrentBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun initUI() {
        binding.hourlyRecycle.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = hourlyAdapter
        }
    }
    private fun setData(it:WeatherResponse){
        hourlyAdapter.updateHours(it.hourly)
        binding.CloudsVal.text=it.current.clouds.toString()
        binding.HumidityVal.text=it.current.humidity.toString()
        binding.PressureVal.text=it.current.pressure.toString()
        binding.SunriseVal.text=timeFormat(it.current.sunrise)
        binding.SunsetVal.text=timeFormat(it.current.sunset)
        binding.WindSpeedVal.text=it.current.wind_speed.toString()
        binding.mainTemperature.text=(it.current.temp.toInt()).toString()+"°c"
        binding.mainViewCountry.text=it.timezone
        binding.mainDescription.text=it.current.weather[0].description
        binding.mainViewDate.text=dateFormat(it.current.dt)
    }
    private fun timeFormat(millisSeconds:Int ): String {
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis((millisSeconds * 1000).toLong())
        val format = SimpleDateFormat("hh:00 aaa")
        return format.format(calendar.time)
    }
    private fun dateFormat(milliSeconds: Int):String{
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds.toLong() * 1000)
        var month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        var day=calendar.get(Calendar.DAY_OF_MONTH).toString()
        var year=calendar.get(Calendar.YEAR).toString()
        return day+"/"+month +"/"+year

    }
    @SuppressLint("MissingPermission")
    fun getLatestLocation() {
        if (isPermissionGranted()) {
            if (checkLocation()) {
                val locationRequest = LocationRequest()
                with(locationRequest) {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = 1000
                }
                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity().application)
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            } else {

                //  val firstTimeLaunch = sharedPref.isFirstTimeLaunch
                /*    if(firstTimeLaunch){
                        val firstTimeLocationEnabled = sharedPref.isFirstTimeLocationEnabled
                        if(firstTimeLocationEnabled) {
                            showErrorDialog("Location","Kindly enable Location to use Application properly")
                        }
                    }*/

            }
        } else {
            requestPermission()
        }
    }



    /* private fun showErrorDialog(alertTitle: String, message: String) {
         val builder = AlertDialog.Builder(requireActivity().application)
         builder.setTitle(alertTitle)
             .setMessage(message)
             .setCancelable(false)
             .setPositiveButton("Enable Location"){
                     dialog, which ->
                 startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                 dialog.dismiss()
             }
             .setNegativeButton("Exit"){
                     dialog, which ->  requireActivity().finish()
                 dialog.dismiss()
             }
             .show()

     }*/

    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(requireActivity().application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity().application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocation(): Boolean {
        val locationManager = requireActivity().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
         //   sharedPref.setFirstTimeLocationenabled(true)
            return true
        }else{
            return false;
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            // TODO use current location long and lat
        //    val lonDecimal = BigDecimal(location.longitude).setScale(4, RoundingMode.HALF_DOWN)
         //   val latDecimal = BigDecimal(location.latitude).setScale(4, RoundingMode.HALF_DOWN)
         //   sharedPref.setLatLong("$latDecimal","$lonDecimal")
        }
    }


    private fun enableLocation() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
           startActivity(intent)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionId) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLatestLocation()
        }
    }

/*    @RequiresApi(Build.VERSION_CODES.M)
    fun CheckStatus(){
        if(!isConnected()){
            showDialog()
            Toast.makeText(requireActivity(), "connection failed", Toast.LENGTH_LONG)
                .show()
        }
    }*/
    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.M)
    fun isConnected(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                //   Log.i(Constants.LOG_TAG, "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                //  Log.i(Constants.LOG_TAG, "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                //   Log.i(Constants.LOG_TAG, "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
/*
    private fun showDialog(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Please check the Internet to proceed further")
            .setCancelable(false)
            .setPositiveButton("Connect"){
                    dialog, which ->
                ContextCompat.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                dialog.dismiss()
            }
            .setNegativeButton("Exit"){
                    dialog, which ->  requireActivity().finish()
                dialog.dismiss()
            }
            .show()
    }
*/


}