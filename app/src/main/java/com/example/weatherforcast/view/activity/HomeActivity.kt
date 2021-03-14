package com.example.weatherforcast.view.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.weatherforcast.R
import com.example.weatherforcast.databinding.ActivityHomeBinding
import com.example.weatherforcast.databinding.ActivityWeatherDetailsBinding
import com.example.weatherforcast.view.fragment.*
import com.example.weatherforcast.viewModel.CurrentViewModel
import com.google.android.material.tabs.TabLayout
import java.util.*

class HomeActivity : AppCompatActivity() {

    lateinit private var currentFragment: CurrentFragment
    lateinit private var favoriteFragment: FavoriteFragment
    lateinit private var settingsFragment: SettingFragment
    lateinit private var alarmFragment: AlarmFragment
    lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        currentFragment = CurrentFragment()
        favoriteFragment = FavoriteFragment()
        settingsFragment = SettingFragment()
        alarmFragment = AlarmFragment()

        makeCurrentFragment(currentFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.currentFragment -> makeCurrentFragment(currentFragment)
                R.id.favouirateFragment -> makeCurrentFragment(favoriteFragment)
                R.id.settingsFragment -> makeCurrentFragment(settingsFragment)
                R.id.alarmFragment -> makeCurrentFragment(alarmFragment)

            }
            true
        }


    }


    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            commit()
        }

    override fun onResume() {
        super.onResume()
    }
}