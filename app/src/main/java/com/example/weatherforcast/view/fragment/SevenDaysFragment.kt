package com.example.weatherforcast.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforcast.R
import com.example.weatherforcast.databinding.FragmentCurrentBinding
import com.example.weatherforcast.databinding.FragmentSevenDaysBinding
import com.example.weatherforcast.view.adapter.DailyAdapter
import com.example.weatherforcast.view.adapter.HourlyAdapter
import com.example.weatherforcast.viewModel.CurrentViewModel
import com.example.weatherforcast.viewModel.SevenDaysViewModel
import java.util.*

class SevenDaysFragment : Fragment() {
    lateinit var sevenDaysViewModel: SevenDaysViewModel
    lateinit var binding: FragmentSevenDaysBinding
    lateinit var  dailyAdapter: DailyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sevenDaysViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(
            SevenDaysViewModel::class.java)
        sevenDaysViewModel.loadWeatherData().observe(this, {
            it?.let {
                dailyAdapter.updateDays(it.daily)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSevenDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dailyAdapter = DailyAdapter(arrayListOf(),requireActivity())
        initUI()
    }
    private fun initUI() {
          binding.dailyRecycle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = dailyAdapter
        }
    }


}