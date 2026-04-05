package com.srzone.ritu.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.srzone.ritu.Fragments.BlogsFragment
import com.srzone.ritu.Fragments.CategoryBlogsFragment
import com.srzone.ritu.Fragments.HomeFragment
import com.srzone.ritu.Fragments.SettingsFragment

class FragmentsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(i: Int): Fragment {
        return when (i) {
            1 -> BlogsFragment()
            2 -> CategoryBlogsFragment()
            3 -> SettingsFragment()
            else -> HomeFragment()
        }
    }


}
