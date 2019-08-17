package com.example.pokedex.view.list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.pokedex.base.util.Region

class PokedexPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val currentRegion = Region.values()[position]

        return RegionalDexFragment.newInstance(currentRegion)
    }

    override fun getCount(): Int {
        return Region.values().size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return Region.values()[position].title
    }
}