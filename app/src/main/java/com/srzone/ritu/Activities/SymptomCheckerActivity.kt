package com.srzone.ritu.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.srzone.ritu.Data.SymptomData
import com.srzone.ritu.Data.SymptomData.Symptom
import com.srzone.ritu.R
import com.srzone.ritu.Utils.LanguageUtils
import com.srzone.ritu.databinding.ActivitySymptomCheckerBinding

class SymptomCheckerActivity : BaseActivity() {

    private lateinit var binding: ActivitySymptomCheckerBinding
    private var isNepali = false

    // State
    private var currentStep = 1
    private var selectedSymptom: Symptom? = null
    private val selectedSecondaryIds = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySymptomCheckerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isNepali = LanguageUtils.getSavedLanguage(this) == "ne"

        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.actionBtn.setOnClickListener { onActionButtonClicked() }

        showStep1()
    }

    // ── STEP 1: Primary symptom chips ──────────────────────────────────────

    private fun showStep1() {
        currentStep = 1
        selectedSymptom = null
        selectedSecondaryIds.clear()

        updateStepIndicator()
        binding.stepHeading.setText(R.string.step1_heading)
        binding.stepSubtitle.visibility = View.GONE

        binding.chipsCard.visibility = View.VISIBLE
        binding.resultCard.visibility = View.GONE
        binding.seeDoctorCard.visibility = View.GONE

        binding.actionBtn.setText(R.string.btn_next)
        binding.actionBtn.isEnabled = false
        binding.actionBtn.alpha = 0.5f

        binding.chipGroup.removeAllViews()
        binding.chipGroup.isSingleSelection = true

        SymptomData.symptoms.forEach { symptom ->
            val chip = buildChip(if (isNepali) symptom.labelNe else symptom.labelEn)
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedSymptom = symptom
                    enableActionButton()
                }
            }
            binding.chipGroup.addView(chip)
        }

        scrollToTop()
    }

    // ── STEP 2: Secondary symptoms ─────────────────────────────────────────

    private fun showStep2() {
        currentStep = 2
        selectedSecondaryIds.clear()

        updateStepIndicator()
        binding.stepHeading.setText(R.string.step2_heading)
        binding.stepSubtitle.setText(R.string.step2_subtitle)
        binding.stepSubtitle.visibility = View.VISIBLE

        binding.chipsCard.visibility = View.VISIBLE
        binding.resultCard.visibility = View.GONE
        binding.seeDoctorCard.visibility = View.GONE

        binding.actionBtn.setText(R.string.btn_see_results)
        // Step 2 is optional — always allow proceeding
        enableActionButton()

        binding.chipGroup.removeAllViews()
        binding.chipGroup.isSingleSelection = false

        selectedSymptom?.secondarySymptoms?.forEach { secondary ->
            val chip = buildChip(if (isNepali) secondary.labelNe else secondary.labelEn)
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedSecondaryIds.add(secondary.id)
                else selectedSecondaryIds.remove(secondary.id)
            }
            binding.chipGroup.addView(chip)
        }

        scrollToTop()
    }

    // ── STEP 3: Results ────────────────────────────────────────────────────

    private fun showStep3() {
        currentStep = 3
        val symptom = selectedSymptom ?: return

        updateStepIndicator()
        binding.stepHeading.setText(R.string.step3_heading)
        binding.stepSubtitle.visibility = View.GONE

        binding.chipsCard.visibility = View.GONE
        binding.resultCard.visibility = View.VISIBLE
        binding.seeDoctorCard.visibility = View.VISIBLE

        binding.actionBtn.setText(R.string.btn_start_over)
        enableActionButton()

        // Description
        binding.resultDescriptionTv.text =
            if (isNepali) symptom.descriptionNe else symptom.descriptionEn

        // Causes
        binding.causesContainer.removeAllViews()
        val causes = if (isNepali) symptom.causesNe else symptom.causesEn
        causes.forEach { cause -> binding.causesContainer.addView(buildBulletRow(cause, "#5C6BC0")) }

        // Remedies
        binding.resultRemediesContainer.removeAllViews()
        val remedies = if (isNepali) symptom.remediesNe else symptom.remediesEn
        remedies.forEach { remedy -> binding.resultRemediesContainer.addView(buildBulletRow(remedy, "#D4537E")) }

        // See a doctor
        binding.seeDoctorTv.text =
            if (isNepali) symptom.seeDoctorNe else symptom.seeDoctorEn

        scrollToTop()
    }

    // ── NAVIGATION ─────────────────────────────────────────────────────────

    private fun onActionButtonClicked() {
        when (currentStep) {
            1 -> if (selectedSymptom != null) showStep2()
            2 -> showStep3()
            3 -> showStep1()
        }
    }

    override fun onBackPressed() {
        when (currentStep) {
            1 -> super.onBackPressed()
            2 -> showStep1()
            3 -> showStep2()
        }
    }

    // ── HELPERS ────────────────────────────────────────────────────────────

    private fun buildChip(label: String): Chip {
        return Chip(this).apply {
            text = label
            isCheckable = true
            isCheckedIconVisible = false
            chipBackgroundColor = ContextCompat.getColorStateList(
                this@SymptomCheckerActivity, R.color.chip_bg_selector)
            setTextColor(ContextCompat.getColorStateList(
                this@SymptomCheckerActivity, R.color.chip_text_selector))
            typeface = resources.getFont(R.font.app_font)
            textSize = 13f
            chipCornerRadius = 24f
            chipStrokeWidth = 1f
            setChipStrokeColorResource(R.color.chip_stroke_selector)
        }
    }

    private fun buildBulletRow(text: String, dotColorHex: String): View {
        val item = LayoutInflater.from(this)
            .inflate(R.layout.item_remedy, null, false)
        item.findViewById<TextView>(R.id.remedyText).text = text
        // Reuse item_remedy.xml — dot color is already pink (#D4537E) from PainSOS,
        // which works fine for remedies. Causes will use the same layout; that's acceptable.
        return item
    }

    private fun enableActionButton() {
        binding.actionBtn.isEnabled = true
        binding.actionBtn.alpha = 1f
    }

    private fun updateStepIndicator() {
        val active = "#D4537E"
        val inactive = "#F0E6EA"
        binding.dot1.backgroundTintList = tint(if (currentStep >= 1) active else inactive)
        binding.dot2.backgroundTintList = tint(if (currentStep >= 2) active else inactive)
        binding.dot3.backgroundTintList = tint(if (currentStep >= 3) active else inactive)
    }

    private fun tint(hex: String) =
        android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(hex))

    private fun scrollToTop() {
        binding.scrollView.post { binding.scrollView.smoothScrollTo(0, 0) }
    }
}