package com.srzone.ritu.OnBoardingScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.srzone.ritu.Activities.InputActivity
import com.srzone.ritu.R
import com.srzone.ritu.databinding.FragmentPeriodInputBinding

class PeriodInputFragment : Fragment() {
    var binding: FragmentPeriodInputBinding? = null

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        val inflate = FragmentPeriodInputBinding.inflate(layoutInflater)
        this.binding = inflate
        inflate.nextSessionBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@PeriodInputFragment.m146x41c2179c(view)
            }
        })
        this.binding!!.backSessionBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@PeriodInputFragment.m147x9acd631d(view)
            }
        })
        this.binding!!.nextBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@PeriodInputFragment.m148xf3d8ae9e(view)
            }
        })
        this.binding!!.prevBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@PeriodInputFragment.m149x4ce3fa1f(view)
            }
        })
        return this.binding!!.getRoot()
    }


    fun m146x41c2179c(view: View?) {
        val inputActivity = requireActivity() as InputActivity
        inputActivity.periodLength = this.binding!!.cycleLengthInp.getText().toString().toInt()
        (inputActivity.findViewById<View?>(R.id.viewPager) as ViewPager2).setCurrentItem(2, true)
    }


    fun m147x9acd631d(view: View?) {
        (requireActivity().findViewById<View?>(R.id.viewPager) as ViewPager2).setCurrentItem(
            0,
            true
        )
    }


    fun m148xf3d8ae9e(view: View?) {
        if (this.binding!!.cycleLengthInp.getText().toString().toInt() < 9) {
            this.binding!!.cycleLengthInp.setText(
                (this.binding!!.cycleLengthInp.getText().toString().toInt() + 1).toString()
            )
        } else {
            this.binding!!.cycleLengthInp.setText(3.toString())
        }
    }


    fun m149x4ce3fa1f(view: View?) {
        if (this.binding!!.cycleLengthInp.getText().toString().toInt() > 3) {
            this.binding!!.cycleLengthInp.setText(
                (this.binding!!.cycleLengthInp.getText().toString().toInt() - 1).toString()
            )
        } else {
            this.binding!!.cycleLengthInp.setText(9.toString())
        }
    }
}
