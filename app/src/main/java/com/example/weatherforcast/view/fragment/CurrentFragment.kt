package com.example.weatherforcast.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
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
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforcast.databinding.FragmentCurrentBinding
import com.example.weatherforcast.model.Alerts
import com.example.weatherforcast.model.WeatherNotification
import com.example.weatherforcast.model.WeatherResponse
import com.example.weatherforcast.view.adapter.DailyAdapter
import com.example.weatherforcast.view.adapter.HourlyAdapter
import com.example.weatherforcast.viewModel.CurrentViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class CurrentFragment : Fragment() {
    lateinit var currentViewModel: CurrentViewModel
    lateinit var binding: FragmentCurrentBinding
    val PermissionId = 1
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var  dailyAdapter: DailyAdapter
    lateinit var lang:String
    lateinit var unit:String
    lateinit var tempUnit:String
    lateinit var windSpeedUnit:String
    private lateinit var notificationUtils: WeatherNotification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences("weather", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        firstTime()
        lang=sharedPref.getString("lang","en").toString()
        unit=sharedPref.getString("units","metric").toString()
        setLocale(lang)
        setUnits(unit)
        getLatestLocation()
        Toast.makeText(
            context,
            "" + sharedPref.getString("lat", "0") + "," + sharedPref.getString("lng", "0"),
            Toast.LENGTH_SHORT
        ).show()

        // editor.putBoolean("isFirstTimeLaunch", true)
        editor.commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hourlyAdapter = HourlyAdapter(arrayListOf(),requireActivity())
        dailyAdapter = DailyAdapter(arrayListOf(),requireActivity())

        initUI()
        currentViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(
            CurrentViewModel::class.java
        )
        currentViewModel.loadWeatherData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it!=null) {
                setData(it)
            }
        })

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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
        }
        binding.dailyRecycle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = dailyAdapter
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(it: WeatherResponse) {
        editor.putString("timezone",it.timezone)
        hourlyAdapter.updateHours(it.hourly)
        dailyAdapter.updateDays(it.daily)
       // Picasso.get().load(iconLinkgetter(it.current.weather[0].icon)).into(binding.MainIcon)
        binding.mainDescription.text = it.current.weather[0].description
        binding.mainViewDate.text = dateFormat(it.current.dt)
        binding.SunriseVal.text = timeFormat(it.current.sunrise)
        binding.SunsetVal.text = timeFormat(it.current.sunset)
        binding.mainViewCountry.text = convertTimezone(it)
        it.alerts?.let {
            notifyUser(it)
        }
        editor.putString("timezone", it.timezone)
        editor.commit()
        if(lang.equals("en")) {
            binding.HumidityVal.text = it.current.humidity.toString()+ "%"
            binding.PressureVal.text = it.current.pressure.toString()+ " hPa"
            binding.WindSpeedVal.text = it.current.wind_speed.toString()  +" "+windSpeedUnit
            binding.mainTemperature.text = (it.current.temp.toInt()).toString() + tempUnit
            binding.CloudsVal.text = it.current.clouds.toString()

        }
        else
        {
            binding.CloudsVal.text = convertToArabic(it.current.clouds)
            binding.HumidityVal.text = convertToArabic(it.current.humidity) + "%"
            binding.PressureVal.text = convertToArabic(it.current.pressure) +"hPa"
            binding.WindSpeedVal.text = convertToArabic(it.current.wind_speed.toInt())+windSpeedUnit
            binding.mainTemperature.text = convertToArabic(it.current.temp.toInt()) +tempUnit


        }

    }
    private fun notifyUser(alert: List<Alerts>) {
        notificationUtils = WeatherNotification(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nb: NotificationCompat.Builder? = notificationUtils.getAndroidChannelNotification(
                alert.get(0)?.event, ""
                        +dateFormat(alert.get(0)?.start.toInt()) + "," +dateFormat(alert.get(0)?.end.toInt()) + "\n" + alert.get(
                    0
                )?.description, true,false
            )
            notificationUtils.getManager()?.notify(3, nb?.build())
        }
    }

    private fun timeFormat(millisSeconds: Int): String {
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis((millisSeconds * 1000).toLong())
        if(lang.equals("en")){
        val format = SimpleDateFormat("hh:00 aaa")
        return format.format(calendar.time)}
        else{
            val format = SimpleDateFormat("۰۰:hh aaa")
            return format.format(calendar.time)}
        }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFormat(milliSeconds: Int): String {
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds.toLong() * 1000)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var year = calendar.get(Calendar.YEAR)
        if(lang.equals("en")) {
            return day.toString() + "/" + month + "/" + year
        }
        else
        {
         return convertToArabic(day)+"/"+convertToArabic(month)+"/"+convertToArabic(year)
        }

    }
    fun setUnits(unit:String)
    {
        when (unit) {
            "metric" -> {
                tempUnit="°c"
                windSpeedUnit="m/s"
            }
            "imperial" -> {
                tempUnit = "°f"
                windSpeedUnit="m/h"
            }
            "standard" ->{
                tempUnit="°k"
                windSpeedUnit="m/s"
            }

        }
    }
    fun setLocale(languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = requireActivity().resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
    fun convertToArabic(value: Int): String? {
        return (value.toString() + "")
            .replace("1", "١").replace("2", "٢")
            .replace("3", "٣").replace("4", "٤")
            .replace("5", "٥").replace("6", "٦")
            .replace("7", "٧").replace("8", "٨")
            .replace("9", "٩").replace("0", "٠")
    }
    fun convertTimezone(weatherResponse: WeatherResponse):String{
        var arabicTimezone=""
        var addressList: List<Address>? = null

        val geocoder=Geocoder(context, Locale(lang))
        try {
            addressList = geocoder.getFromLocation(weatherResponse.lat,weatherResponse.lon, 1)
        }
        catch (e:IOException)
        {
            e.printStackTrace()
        }
   if(addressList?.size!! >0) {
       val address = addressList!![0]
       arabicTimezone = address.locality + "/" + address.adminArea
   }
        return arabicTimezone
    }
    fun iconLinkgetter(iconName:String):String="https://openweathermap.org/img/wn/"+iconName+"@2x.png"
    @SuppressLint("MissingPermission")
    fun getLatestLocation() {
        if (isPermissionGranted()) {
            if (checkLocation()) {
                val locationRequest = LocationRequest()
                with(locationRequest) {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = 1000
                }
                val fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity().application)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                val firstTimeLaunch = sharedPref.getBoolean("isFirstTimeLaunch", true)
                if (firstTimeLaunch) {
                    val firstTimeLocationEnabled =
                        sharedPref.getBoolean("isFirstTimeLocationEnabled", true)
                    if (firstTimeLocationEnabled) {
                        showErrorDialog(
                            "Location",
                            "Kindly enable Location to use Application properly"
                        )
                    }

                }
                enableLocation()
                editor.putBoolean("isFirstTimeLaunch", false)
            }
        } else {
            requestPermission()
        }
    }

    private fun showErrorDialog(alertTitle: String, message: String) {
        val builder = AlertDialog.Builder(requireActivity().application)
        builder.setTitle(alertTitle)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Enable Location") { dialog, which ->
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                dialog.dismiss()
            }
            .setNegativeButton("Exit") { dialog, which ->
                requireActivity().finish()
                dialog.dismiss()
            }
            .show()

    }

    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity().application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireActivity().application,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocation(): Boolean {
        val locationManager =
            requireActivity().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            editor.putBoolean("isFirstTimeLocationEnabled", true)
            return true
        } else {
            return false;
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            // TODO use current location long and lat
            val lonDecimal = BigDecimal(location.longitude).setScale(4, RoundingMode.HALF_DOWN)
            val latDecimal = BigDecimal(location.latitude).setScale(4, RoundingMode.HALF_DOWN)
//            Toast.makeText(context, "" + lonDecimal + "," + latDecimal, Toast.LENGTH_SHORT).show()
            editor.putString("lat", "$latDecimal")
            editor.putString("lng", "$lonDecimal")
            editor.commit()
        }
    }


    private fun enableLocation() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionId) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLatestLocation()
        }
    }
/*
    @RequiresApi(Build.VERSION_CODES.M)
    fun CheckStatus() {
        if (!isConnected()) {
            showDialog()
            Toast.makeText(requireActivity(), "connection failed", Toast.LENGTH_LONG)
                .show()
        }
    }

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.M)
    fun isConnected(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                return true
            }
        }
        return false
    }
    */


    private fun showDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Please check the Internet to proceed further")
            .setCancelable(false)
            .setPositiveButton("Connect") { dialog, which ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                dialog.dismiss()
            }
            .setNegativeButton("Exit") { dialog, which ->
                requireActivity().finish()
                dialog.dismiss()
            }
            .show()
    }
    private fun firstTime(){
        var isFirst=sharedPref.getBoolean("isFirstTimeLaunch",true)
        if(isFirst)
        {
            getLatestLocation()
            editor.putString("lang","en")
            editor.putString("units","metric")
            editor.putBoolean("isFirstTimeLaunch",false)
            editor.commit()
        }
    }


}