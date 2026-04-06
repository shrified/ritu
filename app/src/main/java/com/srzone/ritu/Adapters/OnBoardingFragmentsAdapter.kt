package com.srzone.ritu.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.srzone.ritu.OnBoardingScreen.LanguageSelectFragment
import com.srzone.ritu.OnBoardingScreen.LastPeriodInputFragment
import com.srzone.ritu.OnBoardingScreen.MenstrualCycleInpFragment
import com.srzone.ritu.OnBoardingScreen.PeriodInputFragment

class OnBoardingFragmentsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int = 4

    override fun createFragment(i: Int): Fragment = when (i) {
        0 -> LanguageSelectFragment()
        1 -> MenstrualCycleInpFragment()
        2 -> PeriodInputFragment()
        3 -> LastPeriodInputFragment()
        else -> LanguageSelectFragment()
    }

}
