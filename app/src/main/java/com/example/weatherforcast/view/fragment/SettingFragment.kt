package com.example.weatherforcast.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforcast.databinding.FragmentSettingBinding
import com.example.weatherforcast.view.activity.HomeActivity
import com.example.weatherforcast.viewModel.FavoriteViewModel
import com.example.weatherforcast.viewModel.SettingViewModel
import java.util.*


class SettingFragment : Fragment() {
    lateinit var binding:FragmentSettingBinding
    lateinit var sharedPref:SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var settingViewModel:SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences("weather", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        settingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(
            SettingViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var lang=sharedPref.getString("lang","en")
        var unit=sharedPref.getString("units","metric")

        when (lang) {
            "en" -> binding.EnglishRdBtn.isChecked=true
            "ar" -> binding.ArabicRdBtn.isChecked=true
        }
        when (unit) {
            "metric" -> binding.cRdBtn.isChecked=true
            "imperial" -> binding.fRdBtn.isChecked=true
            "standard" -> binding.kRdBtn.isChecked=true
        }
        binding.cRdBtn.setOnClickListener(View.OnClickListener {
            changeUnit("metric")
        })
        binding.kRdBtn.setOnClickListener(View.OnClickListener {
            changeUnit("standard")
        })
        binding.fRdBtn.setOnClickListener(View.OnClickListener {
           changeUnit("imperial")
        })
        binding.ArabicRdBtn.setOnClickListener(View.OnClickListener {
            changeLang("ar")
        })
        binding.EnglishRdBtn.setOnClickListener(View.OnClickListener {
            changeLang("en")
        })

    }
    private fun changeLang(lang:String){
        editor.putString("lang",lang)
        editor.commit()
        settingViewModel.refreshData()
        setLocale(lang)
        restartApp()
    }
    private fun changeUnit(unit:String){
        editor.putString("units",unit)
        editor.commit()
        settingViewModel.refreshData()
        restartApp()
    }
    private fun restartApp()
    {
        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
       // activity?.finish()
        Runtime.getRuntime().exit(0)

    }
    fun setLocale(languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = requireActivity().resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }


}