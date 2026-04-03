package com.srzone.ritu.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.srzone.ritu.OnBoardingScreen.LastPeriodInputFragment
import com.srzone.ritu.OnBoardingScreen.MenstrualCycleInpFragment
import com.srzone.ritu.OnBoardingScreen.PeriodInputFragment

class OnBoardingFragmentsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(i: Int): Fragment {
        if (i != 1) {
            if (i == 2) {
                return LastPeriodInputFragment()
            }
            return MenstrualCycleInpFragment()
        }
        return PeriodInputFragment()
    }
}
