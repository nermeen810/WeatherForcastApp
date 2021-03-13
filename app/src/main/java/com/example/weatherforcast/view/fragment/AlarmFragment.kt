package com.example.weatherforcast.view.fragment

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforcast.R
import com.example.weatherforcast.databinding.AlarmDialogBinding
import com.example.weatherforcast.databinding.FragmentAlarmBinding
import com.example.weatherforcast.databinding.FragmentSettingBinding
import com.example.weatherforcast.model.Alarm
import com.example.weatherforcast.model.myAlarmReceiver
import com.example.wetherforecastapp.ViewModels.AlarmViewModel
import com.example.wetherforecastapp.ui.adapters.AlarmAdapter
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AlarmFragment : Fragment() {
    private lateinit var viewModel: AlarmViewModel
    private lateinit var binding: FragmentAlarmBinding
    private lateinit var alertAdabter: AlarmAdapter
    private lateinit var bindingDialog: AlarmDialogBinding
    private lateinit var dialog: Dialog
    private var calStart = Calendar.getInstance()
    private var calEnd = Calendar.getInstance()
    private var isedit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(
            AlarmViewModel::class.java
        )
        alertAdabter = AlarmAdapter(arrayListOf(), viewModel, activity?.applicationContext!!)

        viewModel.getNavigate().observe(this,{
            isedit = true
            showDialog(it)
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()//init recylcer view
        getAlarmData(viewModel)
        rvSwipeListener()
        binding.addBtn.setOnClickListener { v ->
            var alarmObj = Alarm("", "", "", "", true, "")
            showDialog(alarmObj)
        }
    }
    private fun getAlarmData(viewModel: AlarmViewModel) {
        viewModel.getAlarmList().observe(this) {
            alertAdabter.updateAlarms(it)

        }
    }

    private fun showDialog(alarmObj: Alarm) {
        dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        // change dialog size
        lp.copyFrom(dialog.getWindow()?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        bindingDialog = AlarmDialogBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        if (isedit) {
            bindingDialog.DescribtionTv.setText(alarmObj.description)
            bindingDialog.calenderBtn.text = alarmObj.Date
            bindingDialog.fromTimeImg.text = alarmObj.start
            bindingDialog.toTimeImg.text = alarmObj.end
            isedit = false

        }
        var current = Calendar.getInstance()

        bindingDialog.fromTimeImg.setOnClickListener {
            val tpd1 = TimePickerDialog(
                context,
                { view, h, m ->
                    calStart.set(Calendar.HOUR_OF_DAY, h)
                    calStart.set(Calendar.MINUTE, m)
                    calStart.set(Calendar.SECOND, 0)
                    // alarmObj.start = "$h : $m"
                    val format = SimpleDateFormat("hh:mm aaa")
                    alarmObj.start = format.format(calStart.time)
                    bindingDialog.fromTimeImg.setText(format.format(calStart.time))
                    Log.i("alarm", "ss" + format.format(calStart.time))
                },
                current.get(Calendar.HOUR_OF_DAY),
                current.get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(context?.applicationContext)
            )
            tpd1.show()
        }

        bindingDialog.toTimeImg.setOnClickListener {
            val tpd = TimePickerDialog(
                context,
                { view, h, m ->
                    calEnd.set(Calendar.HOUR_OF_DAY, h)
                    calEnd.set(Calendar.MINUTE, m)
                    calEnd.set(Calendar.SECOND, 0)
                    // alarmObj.end = "$h : $m"
                    val format = SimpleDateFormat("hh:mm aaa")
                    alarmObj.end = format.format(calEnd.time)
                    bindingDialog.toTimeImg.setText(format.format(calEnd.time))
                    bindingDialog.toTimeImg.setText(format.format(calEnd.time))
                    Log.i("alarm", "l" + format.format(calEnd.time))

                },
                current.get(Calendar.HOUR_OF_DAY),
                current.get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(context?.applicationContext)
            )
            tpd.show()
        }

        bindingDialog.calenderBtn.setOnClickListener {
            val dateSetListener = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    calStart.set(Calendar.YEAR, year)
                    calStart.set(Calendar.MONTH, monthOfYear)
                    calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    calEnd.set(Calendar.YEAR, year)
                    calEnd.set(Calendar.MONTH, monthOfYear)
                    calEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val dateFormant = SimpleDateFormat("dd-MM-yyyy")
                    val currentDate = dateFormant.parse(dateFormant.format(current.getTime()))
                    val selectedDate = dateFormant.parse(dateFormant.format(calStart.getTime()))
                    if (currentDate.after(selectedDate) && (!currentDate.equals(selectedDate))) {
                        Toast.makeText(context, "Set valid date", Toast.LENGTH_LONG).show()
                    } else {
                        val myFormat = "dd/MM/yyyy" // mention the format you need
                        val sdf = SimpleDateFormat(myFormat, Locale.US)
                        alarmObj.Date = sdf.format(calStart.time)
                        val chosenDate = DateFormat.getDateInstance().format(calStart.getTime())
                        bindingDialog.calenderBtn.setText(chosenDate)
                        Log.i("alarm", "dd" + chosenDate)
                    }

                },
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH)
            )

            dateSetListener.show()
        }

        bindingDialog.addAlarmBtn.setOnClickListener {
            if (validateDialog()) {
                if (calStart.timeInMillis < calEnd.timeInMillis) {
                    alarmObj.description = bindingDialog.DescribtionTv.text.toString()
                    alarmObj.event = getEvent()

                    when (bindingDialog.soundSpinner.getSelectedItemPosition()) {
                        0 -> alarmObj.sound = true// notification
                        1 -> alarmObj.sound = false // alarm loopSound
                    }
                    var id = 0
                    var jop = CoroutineScope(Dispatchers.IO).launch {
                        id = viewModel.insertAlarmObj(alarmObj).toInt()
                    }
                    jop.invokeOnCompletion {
                        setAlarm(
                            context?.applicationContext!!,
                            id,
                            calStart,
                            calEnd,
                            alarmObj.event,
                            alarmObj.sound
                        )
                    }
                    dialog.dismiss()

                } else {
                    Toast.makeText(
                        context,
                        "Please Make Sure Your Timing is correct",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }

        bindingDialog.closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.getWindow()?.setAttributes(lp)
    }

    private fun setAlarm(
        context: Context,
        id: Int,
        calStart: Calendar,
        calEnd: Calendar,
        event: String,
        sound: Boolean
    ) {
        val mIntent = Intent(context, myAlarmReceiver::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        mIntent.putExtra("endTime", calEnd.timeInMillis)
        mIntent.putExtra("id", id)
        mIntent.putExtra("event", event)
        mIntent.putExtra("sound", sound)
        val mPendingIntent =
            PendingIntent.getBroadcast(context, id, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calStart.timeInMillis,
            2 * 1000, mPendingIntent
        )
    }

    private fun getEvent(): String {
        var event = ""
        var arr = this.resources.getStringArray(R.array.event_options)
        when (bindingDialog.eventSpinner.getSelectedItemPosition()) {
            0 -> event = arr[0]
            1 -> event = arr[1]
            2 -> event = arr[2]
            3 -> event = arr[3]
            4 -> event = arr[4]
            5 -> event = arr[5]
            6 -> event = arr[6]
        }
        return event
    }

    private fun initUI() {
        binding.alarmList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = alertAdabter

        }
    }

    private fun validateDialog(): Boolean {
        if (bindingDialog.toTime.text.isEmpty())
            Toast.makeText(context, getString(R.string.empty_time), Toast.LENGTH_SHORT).show()
        else if (bindingDialog.fromTime.text.isEmpty())
            Toast.makeText(context, getString(R.string.empty_time), Toast.LENGTH_SHORT).show()
        else if (bindingDialog.calenderBtn.text.isEmpty())
            Toast.makeText(context, getString(R.string.empty_date), Toast.LENGTH_SHORT).show()
        else
            return true
        return false
    }

    private fun rvSwipeListener() {
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item: Alarm = alertAdabter.getItemAt(position)
                alertAdabter.alarmList.removeAt(position)
                alertAdabter.notifyItemRemoved(position);
                //Toast.makeText(applicationContext, "deleted", Toast.LENGTH_SHORT).show()
                Snackbar.make(binding.alarmList, "deleted", Snackbar.LENGTH_SHORT).apply {
                    setAction("UNDO") {
                        alertAdabter.alarmList.add(item)
                        alertAdabter.notifyItemInserted(position);
                    }
                    addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onShown(transientBottomBar: Snackbar?) {
                            super.onShown(transientBottomBar)
                            Log.i("snack", "onShown")
                        }

                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            // Snackbar closed on its own
                            Log.i("snack", "on click")
                            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                // Snackbar closed on its own
                                Log.i("snack", "onDismissed")
                                viewModel.deleteAlarmObj(item.id)
                                val intent = Intent(context, myAlarmReceiver::class.java)
                                val pendingIntent = PendingIntent.getBroadcast(
                                    context,
                                    item.id,
                                    intent,
                                    0
                                )
                                val alarmManager =
                                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                alarmManager.cancel(pendingIntent)
                            }

                        }
                    })
                    setTextColor(Color.parseColor("#FFFFFFFF"))
                    setActionTextColor(Color.parseColor("#FFBB86FC"))
                    setBackgroundTint(Color.parseColor("#616161"))
                    duration.minus(1)
                }.show()

            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.alarmList)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }


}