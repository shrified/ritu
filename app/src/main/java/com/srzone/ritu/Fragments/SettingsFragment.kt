package com.srzone.ritu.Fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.Activities.InputActivity
import com.srzone.ritu.Activities.MainActivity
import com.srzone.ritu.Adapters.CustomThemesSelectorAdapter
import com.srzone.ritu.R
import com.srzone.ritu.ThemesFiles.MyCustomTheme
import com.srzone.ritu.ThemesFiles.MyThemeHandler
import com.srzone.ritu.Utils.OvulationCalculations
import com.srzone.ritu.Utils.SharedPreferenceUtils
import com.srzone.ritu.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var adapter: CustomThemesSelectorAdapter? = null
    private var binding: FragmentSettingsBinding? = null
    private val handler = MyThemeHandler()

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, viewGroup, false)
        
        setData()
        
        binding?.recalculateBtn?.setOnClickListener {
            onRecalculateClicked()
        }
        
        binding?.shareUsBtn?.setOnClickListener {
            onShareUsClicked()
        }
        
        binding?.rateUsBtn?.setOnClickListener {
            onRateUsClicked()
        }
        
        binding?.privacyPolicyBtn?.setOnClickListener {
            onPrivacyPolicyClicked()
        }
        
        setUpThemesRecycler()
        setUpTheme()
        
        return binding?.root
    }

    private fun onRecalculateClicked() {
        val intent = Intent(activity, InputActivity::class.java)
        intent.putExtra("recalculate", true)
        startActivity(intent)
    }

    private fun onShareUsClicked() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "SUBJECT")
            putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=${requireActivity().packageName}"
            )
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_us)))
    }

    private fun onRateUsClicked() {
        val packageName = requireActivity().packageName
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (unused: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    private fun onPrivacyPolicyClicked() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com")))
    }

    private fun setUpTheme() {
        val activity = activity ?: return
        val theme = handler.getAppTheme(activity) ?: return
        if (theme.isDark) {
            binding?.headingTv?.setTextColor(ContextCompat.getColor(activity, R.color.white))
        }
    }

    private fun setData() {
        val activity = activity ?: return
        val cycles = SharedPreferenceUtils.getCycles(activity).toInt()
        val date = SharedPreferenceUtils.getDate(activity)
        
        binding?.cycleLengthTv?.text = "$cycles ${getString(R.string.days)}"
        
        binding?.periodLengthTv?.text = 
            "${SharedPreferenceUtils.getCycleLength(activity)} ${getString(R.string.days)}"
        
        val ovulation = OvulationCalculations.getOvulation(date, cycles)
        val nextPeriod = OvulationCalculations.getNextPeriod(date, cycles)
        
        val daysBetweenTwoDates = OvulationCalculations.daysBetweenTwoDates(ovulation, nextPeriod).toInt()
        
        binding?.lutealPhaseTv?.text = "$daysBetweenTwoDates ${getString(R.string.days)}"
    }

    private fun setUpThemesRecycler() {
        val activity = activity ?: return
        
        val themeList = ArrayList(MyThemeHandler.CUSTOM_THEMES.toList())
        
        adapter = object : CustomThemesSelectorAdapter(
            activity,
            themeList,
            handler.getAppThemeIndex(activity)
        ) {
            override fun onThemeItemSelected(myCustomTheme: MyCustomTheme?) {
                this@SettingsFragment.handler.setAppTheme(
                    this@SettingsFragment.adapter?.selectedTheme,
                    activity
                )
                this@SettingsFragment.startActivity(
                    Intent(
                        this@SettingsFragment.requireContext(),
                        MainActivity::class.java
                    )
                )
                activity.finishAffinity()
            }
        }
        
        binding?.themesRecycler?.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = this@SettingsFragment.adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
