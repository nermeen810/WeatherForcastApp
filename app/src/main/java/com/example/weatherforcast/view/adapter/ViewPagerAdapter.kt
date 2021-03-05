package com.example.weatherforcast.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdaptor(
    fm: FragmentManager,
    behavior: Int,
    private val mfragments: List<Fragment>,
    private val mfragmentTitles: List<String>
) :
    FragmentStatePagerAdapter(fm, behavior) {
    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItem(position: Int): Fragment {
        return mfragments[position]
    }

    override fun getCount(): Int {
        return mfragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mfragmentTitles[position]
    }

}
