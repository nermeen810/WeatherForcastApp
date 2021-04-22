package com.example.wetherforecastapp.ui.adapters

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforcast.R
import com.example.weatherforcast.data.localData.DataSource
import com.example.weatherforcast.databinding.AlarmItemBinding
import com.example.weatherforcast.model.Alarm
import com.example.weatherforcast.model.WeatherNotification
import com.example.weatherforcast.model.WeatherResponse
import com.example.weatherforcast.model.myAlarmReceiver
import com.example.wetherforecastapp.ViewModels.AlarmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.util.ArrayList

class AlarmAdapter(
    var alarmList: ArrayList<Alarm>,
    alartViewModel: AlarmViewModel,
    context: Context
) : RecyclerView.Adapter<AlarmAdapter.VH>() {
    var context: Context
    var alartViewModel: AlarmViewModel
    var notificationUtils: WeatherNotification
    var notificationManager: NotificationManager? = null

    init {
        this.context = context
        this.alartViewModel = alartViewModel
        notificationUtils = WeatherNotification(context)
        notificationManager = notificationUtils.getManager()
    }


    fun updateAlarms(newAlarmList: List<Alarm>) {
        alarmList.clear()
        alarmList.addAll(newAlarmList)
        notifyDataSetChanged()
    }

    class VH(var myView: AlarmItemBinding) : RecyclerView.ViewHolder(myView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewBinding =
            AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(viewBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.myView.alarmType.text = alarmList[position].event
        // holder.myView.dateTime.text =
        //    alarmList[position].Date + " " + alarmList[position].start + R.string.to + alarmList[position].end
        holder.myView.details.text = alarmList[position].description

        holder.itemView.setOnClickListener {
            alartViewModel.onEditClick(alarmList[position])
        }
        holder.itemView.setOnLongClickListener {
            showDialog(alarmList[position], position)
            true

        }


    }

    fun showDialog(alarm: Alarm, pos: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.app_name)

        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setNegativeButton(R.string.NoMessage) { dialogInterface, which ->

        }
        builder.setPositiveButton(R.string.yesMessage) { dialogInterface, which ->
            cancelAlarm(alarm.id, context)
            CoroutineScope(Dispatchers.IO).launch {
                val localDataSource = DataSource(context.applicationContext as Application)
                localDataSource.deleteAlarmObj(alarm.id)
            }
            alarmList.remove(alarm)
            notifyItemRemoved(pos)
            updateAlarms(alarmList)
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun cancelAlarm(id: Int, context: Context) {
        notificationManager?.cancel(id)
        Log.i("alarmID", "" + id)
        val intent = Intent(context, myAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    fun getItemAt(pos: Int) = alarmList.get(pos)
    override fun getItemCount() = alarmList.size

}