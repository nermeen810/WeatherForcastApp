package com.example.weatherforcast.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.weatherforcast.R
import com.example.weatherforcast.view.adapter.ViewPagerAdaptor
import com.example.weatherforcast.view.fragment.CurrentFragment
import com.example.weatherforcast.view.fragment.FavoriteFragment
import com.example.weatherforcast.view.fragment.SevenDaysFragment
import com.example.weatherforcast.viewModel.CurrentViewModel
import com.google.android.material.tabs.TabLayout
import java.util.*

class HomeActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var adaptor: ViewPagerAdaptor
    lateinit private var tabLayout: TabLayout
    lateinit private var currentFragment: CurrentFragment
    lateinit  private var sevenDaysFragment: SevenDaysFragment
    lateinit  private var favoriteFragment: FavoriteFragment
    lateinit private var fragments: MutableList<Fragment>
    lateinit private var fragmentTitles: MutableList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        supportActionBar!!.title = "Weathery"

        //inflating views
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        //initializing fragments

        //initializing fragments
        currentFragment = CurrentFragment()
        sevenDaysFragment = SevenDaysFragment()
        favoriteFragment = FavoriteFragment()

        //initializing viewPager

        //initializing viewPager
        tabLayout.setupWithViewPager(viewPager)
        fragmentsinit()
        fragmentTitlesinit()
//        setupTabIcons();
        //        setupTabIcons();
        adaptor = ViewPagerAdaptor(supportFragmentManager, 0, fragments, fragmentTitles)
        viewPager.adapter = adaptor

        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                adaptor.notifyDataSetChanged()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        viewPager.adapter!!.notifyDataSetChanged()

    }
    private fun fragmentTitlesinit() {
        fragmentTitles = ArrayList()
        fragmentTitles.add("Current")
        fragmentTitles.add("7Days")
        fragmentTitles.add("Favorite")
    }

    private fun fragmentsinit() {
        fragments = ArrayList()
        fragments.add(currentFragment)
        fragments.add(sevenDaysFragment)
        fragments.add(favoriteFragment)
    }

    override fun onResume() {
        super.onResume()
        viewPager.adapter!!.notifyDataSetChanged()
    }
}