package com.srzone.ritu.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import com.srzone.ritu.R
import com.srzone.ritu.Utils.LanguageUtils
import com.srzone.ritu.data.ChhaupadiData
import com.srzone.ritu.databinding.ActivityChhaupadiBinding

class ChhaupadiActivity : BaseActivity() {

    private lateinit var binding: ActivityChhaupadiBinding
    private var isNepali = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        // set the status bar icon colors to white
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        binding = ActivityChhaupadiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This is the cleanest, most "Senior" way
        val rootView = findViewById<View>(R.id.rootLayout)
        val statusBarBackground = findViewById<View>(R.id.statusBarBackground)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { rootView, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            // 1. PROTECT THE SIDES: Apply left/right padding to the root layout.
            // This prevents content from hiding under the notch in landscape.
            // We leave top as 0 because we handle it manually below.
            // No Bottom Navigation, so directly pad on the rootView layout
            rootView.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            // 2. Top STATUS BAR: Match the fake status bar height to the top inset
            statusBarBackground.updateLayoutParams {
                height = systemBars.top
            }

            insets
        }

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