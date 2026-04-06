package com.srzone.ritu.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.srzone.ritu.Data.PainSOSData
import com.srzone.ritu.Data.PainSOSData.PainLevel
import com.srzone.ritu.R
import com.srzone.ritu.Utils.LanguageUtils
import com.srzone.ritu.databinding.ActivityPainSosBinding

class PainSOSActivity : BaseActivity() {

    private lateinit var binding: ActivityPainSosBinding
    private var currentLevel: String = "mild"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPainSosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        setupLevelButtons()
        showLevel("mild")   // default
    }

    private fun setupLevelButtons() {
        binding.mildBtn.setOnClickListener     { showLevel("mild") }
        binding.moderateBtn.setOnClickListener { showLevel("moderate") }
        binding.severeBtn.setOnClickListener   { showLevel("severe") }
    }

    private fun showLevel(level: String) {
        currentLevel = level

        // Update button backgrounds
        binding.mildBtn.setBackgroundResource(
            if (level == "mild") R.drawable.pain_level_selected_bg else R.drawable.pain_level_unselected_bg)
        binding.moderateBtn.setBackgroundResource(
            if (level == "moderate") R.drawable.pain_level_selected_bg else R.drawable.pain_level_unselected_bg)
        binding.severeBtn.setBackgroundResource(
            if (level == "severe") R.drawable.pain_level_selected_bg else R.drawable.pain_level_unselected_bg)

        val data: PainLevel = PainSOSData.levels.first { it.level == level }
        val isNepali = LanguageUtils.getSavedLanguage(this) == "ne"
        val remedies = if (isNepali) data.remediesNe else data.remediesEn

        // Populate remedies
        binding.remediesContainer.removeAllViews()
        remedies.forEach { remedy ->
            val item = LayoutInflater.from(this)
                .inflate(R.layout.item_remedy, binding.remediesContainer, false)
            item.findViewById<TextView>(R.id.remedyText).text = remedy
            binding.remediesContainer.addView(item)
        }

        // Warning card — only for severe
        binding.warningCard.visibility = if (data.showWarning) View.VISIBLE else View.GONE
    }
}