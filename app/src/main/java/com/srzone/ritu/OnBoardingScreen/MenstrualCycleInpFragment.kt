package com.srzone.ritu.OnBoardingScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.srzone.ritu.Activities.InputActivity
import com.srzone.ritu.R
import com.srzone.ritu.databinding.FragmentMenstrualCycleInpBinding

class MenstrualCycleInpFragment : Fragment() {
    var binding: FragmentMenstrualCycleInpBinding? = null

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        val inflate = FragmentMenstrualCycleInpBinding.inflate(layoutInflater)
        this.binding = inflate
        inflate.nextSessionBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@MenstrualCycleInpFragment.m143x9c48bafd(view)
            }
        })
        this.binding!!.nextBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@MenstrualCycleInpFragment.m144xdc73a1be(view)
            }
        })
        this.binding!!.prevBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@MenstrualCycleInpFragment.m145x1c9e887f(view)
            }
        })
        return this.binding!!.getRoot()
    }


    fun m143x9c48bafd(view: View?) {
        val inputActivity = requireActivity() as InputActivity
        inputActivity.cyclesLength = this.binding!!.cycleLengthInp.getText().toString().toInt()
        (inputActivity.findViewById<View?>(R.id.viewPager) as ViewPager2).setCurrentItem(1, true)
    }


    fun m144xdc73a1be(view: View?) {
        if (this.binding!!.cycleLengthInp.getText().toString().toInt() < 40) {
            this.binding!!.cycleLengthInp.setText(
                (this.binding!!.cycleLengthInp.getText().toString().toInt() + 1).toString()
            )
        } else {
            this.binding!!.cycleLengthInp.setText(20.toString())
        }
    }


    fun m145x1c9e887f(view: View?) {
        if (this.binding!!.cycleLengthInp.getText().toString().toInt() > 20) {
            this.binding!!.cycleLengthInp.setText(
                (this.binding!!.cycleLengthInp.getText().toString().toInt() - 1).toString()
            )
        } else {
            this.binding!!.cycleLengthInp.setText(40.toString())
        }
    }
}
