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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding?.recalculateBtn?.setOnClickListener { onRecalculateClicked() }
        binding?.shareUsBtn?.setOnClickListener { onShareUsClicked() }
        binding?.rateUsBtn?.setOnClickListener { onRateUsClicked() }
        binding?.privacyPolicyBtn?.setOnClickListener { onPrivacyPolicyClicked() }

        setUpThemesRecycler()
        setUpTheme()

        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        // ✅ Refresh rows every time user comes back to this screen
        setData()
    }

    private fun setData() {
        val activity = activity ?: return

        val cycleLength = SharedPreferenceUtils.getCycleLength(activity)
        val periodLength = SharedPreferenceUtils.getPeriodLength(activity)
        val date = SharedPreferenceUtils.getDate(activity)
        val cycles = SharedPreferenceUtils.getCycles(activity).toIntOrNull() ?: 28

        // ✅ Menstrual cycle = how many days in a cycle (e.g. 28)
        binding?.menstrualCycleRow?.apply {
            rowIcon.setImageResource(R.drawable.ic_menstrual_cycle)
            rowLabel.text = getString(R.string.menstrual_cycle)
            rowValue.text = "$cycleLength ${getString(R.string.days)}"
        }

        // ✅ Period length = how many days period lasts (e.g. 5)
        binding?.periodLengthRow?.apply {
            rowIcon.setImageResource(R.drawable.ic_period_length)
            rowLabel.text = getString(R.string.period_length)
            rowValue.text = "$periodLength ${getString(R.string.days)}"
        }

        // ✅ Luteal phase = days between ovulation and next period
        val ovulation = OvulationCalculations.getOvulation(date, cycles)
        val nextPeriod = OvulationCalculations.getNextPeriod(date, cycles)
        val lutealDays = OvulationCalculations.daysBetweenTwoDates(ovulation, nextPeriod).toInt()

        binding?.lutealPhaseRow?.apply {
            rowIcon.setImageResource(R.drawable.ic_luteal_phase)
            rowLabel.text = getString(R.string.luteal_phase)
            rowValue.text = "$lutealDays ${getString(R.string.days)}"
        }
    }

    private fun onRecalculateClicked() {
        startActivity(Intent(activity, InputActivity::class.java).apply {
            putExtra("recalculate", true)
        })
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
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
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

    private fun setUpThemesRecycler() {
        val activity = activity ?: return
        val themeList = ArrayList(MyThemeHandler.CUSTOM_THEMES.toList())

        adapter = object : CustomThemesSelectorAdapter(
            activity,
            themeList,
            handler.getAppThemeIndex(activity)
        ) {
            override fun onThemeItemSelected(myCustomTheme: MyCustomTheme?) {
                handler.setAppTheme(adapter?.selectedTheme, activity)
                startActivity(
                    Intent(requireContext(), MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
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