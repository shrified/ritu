package com.srzone.ritu.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import com.srzone.ritu.R
import com.srzone.ritu.Utils.LanguageUtils
import com.srzone.ritu.data.ChhaupadiData
import com.srzone.ritu.databinding.ActivityChhaupadiBinding

class ChhaupadiActivity : BaseActivity() {

    private lateinit var binding: ActivityChhaupadiBinding
    private var isNepali = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChhaupadiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isNepali = LanguageUtils.getSavedLanguage(this) == "ne"

        binding.backBtn.setOnClickListener { onBackPressed() }

        setupUI()
        loadMyths()
    }

    private fun setupUI() {
        binding.screenTitle.text =
            if (isNepali) ChhaupadiData.titleNe else ChhaupadiData.titleEn

        binding.infoText.text =
            if (isNepali)
                "छाउपडी नेपालमा २०१७ देखि गैरकानुनी छ। यो प्रथा महिलाको स्वास्थ्यका लागि खतरनाक छ।"
            else
                "Chhaupadi has been illegal in Nepal since 2017. It is dangerous and harmful to women's health."
    }

    private fun loadMyths() {
        val inflater = LayoutInflater.from(this)

        ChhaupadiData.myths.forEach { myth ->
            val view = inflater.inflate(R.layout.item_myth, binding.container, false)

            val mythText = if (isNepali) myth.mythNe else myth.mythEn
            val truthText = if (isNepali) myth.truthNe else myth.truthEn

            view.findViewById<TextView>(R.id.mythTv).text = "❌ $mythText"
            view.findViewById<TextView>(R.id.truthTv).text = "✅ $truthText"

            binding.container.addView(view)
        }
    }
}