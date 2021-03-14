package com.example.weatherforcast.view.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforcast.R
import com.example.weatherforcast.databinding.FavoriteItemBinding
import com.example.weatherforcast.model.WeatherResponse
import com.example.weatherforcast.view.activity.WeatherDetailsActivity
import com.example.weatherforcast.viewModel.FavoriteViewModel
import java.io.IOException
import java.util.*

class FavoriteAdapter(
    private val favoriteList: ArrayList<WeatherResponse>,
    private val context: Context?,
    private var favoriteViewModel: FavoriteViewModel
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    var lang: String
    var sharedPref: SharedPreferences

    init {
        sharedPref = context!!.getSharedPreferences("weather", Context.MODE_PRIVATE)
        lang = sharedPref.getString("lang", "en").toString()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int

    ): FavoriteAdapter.FavoriteViewHolder {
        return FavoriteViewHolder(
            FavoriteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.binding.dailyImg.setImageResource(R.drawable.city)
        holder.binding.timeZoneText.text = convertTimezone(favoriteList[position])
        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, WeatherDetailsActivity::class.java)
            intent.putExtra("timezone", favoriteList[position].timezone)
            context?.startActivity(intent)
        })
        holder.itemView.setOnLongClickListener {
            customTwoButtonsDialog(favoriteList[position], position)
            true
        }
    }

    fun customTwoButtonsDialog(Weather: WeatherResponse, position: Int) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(R.string.app_name)

        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(R.string.yesMessage) { dialogInterface, which ->
            favoriteViewModel.deletWeatherData(favoriteList[position].timezone)
            favoriteList.remove(favoriteList[position])
            notifyItemRemoved(position)
            updateFavorite(favoriteList)
        }

        builder.setNeutralButton(R.string.NoMessage) { dialogInterface, which ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun convertTimezone(weatherResponse: WeatherResponse): String {
        var timezone = ""
        var addressList: List<Address>? = null

        val geocoder = Geocoder(context, Locale(lang))
        try {
            addressList = geocoder.getFromLocation(weatherResponse.lat, weatherResponse.lon, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (addressList?.size!! > 0) {
            val address = addressList!![0]
            Log.i("key", "@@@@@@@@@@@@@@@@@@@@@@@@@@" + address)
            timezone = address.adminArea
        }
        return timezone
    }

    fun iconLinkgetter(iconName: String): String =
        "https://openweathermap.org/img/wn/" + iconName + "@2x.png"

    fun updateFavorite(newFavoriteList: List<WeatherResponse>) {
        favoriteList.clear()
        favoriteList.addAll(newFavoriteList)
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder constructor(val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}