package com.zanfolin.pokedex.feature.list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.zanfolin.pokedex.base.util.Region

class PokedexPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val currentRegion = Region.values()[position]

        return RegionalDexFragment.newInstance(currentRegion)
    }

    override fun getCount() = Region.values().size

    override fun getPageTitle(position: Int) = Region.values()[position].title
}