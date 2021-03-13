package com.example.weatherforcast.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforcast.R
import com.example.weatherforcast.databinding.FragmentFavoriteBinding
import com.example.weatherforcast.databinding.FragmentSevenDaysBinding
import com.example.weatherforcast.view.activity.MapsActivity
import com.example.weatherforcast.view.activity.WeatherDetailsActivity
import com.example.weatherforcast.view.adapter.DailyAdapter
import com.example.weatherforcast.view.adapter.FavoriteAdapter
import com.example.weatherforcast.viewModel.FavoriteViewModel
import com.example.weatherforcast.viewModel.SevenDaysViewModel


class FavoriteFragment : Fragment() {
    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var binding: FragmentFavoriteBinding
    lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(
            FavoriteViewModel::class.java
        )
        favoriteViewModel.loadWeatherData().observe(this, {
            it?.let {
                favoriteAdapter.updateFavorite(it)
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteAdapter = FavoriteAdapter(arrayListOf(),context,favoriteViewModel)
        initUI()
        binding.addFloutBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            context?.startActivity(intent)
        })
    }

    private fun initUI() {
        binding.dailyRecycle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = favoriteAdapter
        }
    }
}