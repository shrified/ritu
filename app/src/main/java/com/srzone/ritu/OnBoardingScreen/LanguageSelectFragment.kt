package com.srzone.ritu.OnBoardingScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.srzone.ritu.R
import com.srzone.ritu.Utils.LanguageUtils
import com.srzone.ritu.databinding.FragmentLanguageSelectBinding

class LanguageSelectFragment : Fragment() {
    private var binding: FragmentLanguageSelectBinding? = null
    private var selectedLang = "en"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLanguageSelectBinding.inflate(inflater, container, false)

        // Default to previously saved or "en"
        selectedLang = LanguageUtils.getSavedLanguage(requireContext())
        updateSelection(selectedLang)

        binding?.englishCard?.setOnClickListener {
            selectedLang = "en"
            updateSelection("en")
        }

        binding?.nepaliCard?.setOnClickListener {
            selectedLang = "ne"
            updateSelection("ne")
        }

        binding?.nextSessionBtn?.setOnClickListener {
            LanguageUtils.saveLanguage(requireContext(), selectedLang)
            requireActivity().findViewById<ViewPager2>(R.id.viewPager)
                ?.setCurrentItem(1, true)
        }

        return binding?.root
    }

    private fun updateSelection(lang: String) {
        val primary = ContextCompat.getColor(requireContext(), R.color.app_primary_color)
        val white = ContextCompat.getColor(requireContext(), R.color.white)
        val transparent = android.graphics.Color.TRANSPARENT

        if (lang == "en") {
            binding?.englishCard?.setCardBackgroundColor(primary)
            binding?.englishCheck?.visibility = View.VISIBLE
            binding?.nepaliCard?.setCardBackgroundColor(white)
            binding?.nepaliCheck?.visibility = View.GONE
        } else {
            binding?.nepaliCard?.setCardBackgroundColor(primary)
            binding?.nepaliCheck?.visibility = View.VISIBLE
            binding?.englishCard?.setCardBackgroundColor(white)
            binding?.englishCheck?.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}