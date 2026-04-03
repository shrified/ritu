package com.srzone.ritu.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.srzone.ritu.Fragments.BlogsFragment
import com.srzone.ritu.Fragments.CalendarFragment
import com.srzone.ritu.Fragments.CategoryBlogsFragment
import com.srzone.ritu.Fragments.HomeFragment
import com.srzone.ritu.Fragments.SettingsFragment

class FragmentsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(i: Int): Fragment {
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        return SettingsFragment()
                    }
                    return HomeFragment()
                }
                return CategoryBlogsFragment()
            }
            return BlogsFragment()
        }
        return CalendarFragment()
    }
}
