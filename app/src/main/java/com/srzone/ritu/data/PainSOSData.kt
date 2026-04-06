package com.srzone.ritu.data

object PainSOSData {

    data class PainLevel(
        val level: String,
        val titleEn: String,
        val titleNe: String,
        val remediesEn: List<String>,
        val remediesNe: List<String>,
        val showWarning: Boolean,
        val warningEn: String = "",
        val warningNe: String = ""
    )

    val levels = listOf(
        PainLevel(
            level = "mild",
            titleEn = "Mild Cramps",
            titleNe = "हल्का पीडा",
            remediesEn = listOf(
                "Place a hot water bottle on your lower abdomen",
                "Light walking or gentle stretching",
                "Drink ginger or cinnamon tea",
                "Rest and stay warm"
            ),
            remediesNe = listOf(
                "पेटको तल्लो भागमा तातो पानीको बोतल राख्नुहोस्",
                "हल्का हिँडाइ वा स्ट्रेचिङ गर्नुहोस्",
                "अदुवा वा दालचिनीको चिया पिउनुहोस्",
                "आराम गर्नुहोस् र न्यानो रहनुहोस्"
            ),
            showWarning = false
        ),
        PainLevel(
            level = "moderate",
            titleEn = "Moderate Pain",
            titleNe = "मध्यम पीडा",
            remediesEn = listOf(
                "Rest with a hot compress on abdomen",
                "Avoid cold water and cold foods",
                "Eat light warm meals",
                "Deep breathing exercises",
                "If available, take ibuprofen with food"
            ),
            remediesNe = listOf(
                "पेटमा तातो सेक गरी आराम गर्नुहोस्",
                "चिसो पानी र चिसो खाना नखानुहोस्",
                "हल्का तातो खाना खानुहोस्",
                "गहिरो सास फेर्ने व्यायाम गर्नुहोस्",
                "उपलब्ध भएमा खाना खाएपछि आइबुप्रोफेन लिनुहोस्"
            ),
            showWarning = false
        ),
        PainLevel(
            level = "severe",
            titleEn = "Severe Pain",
            titleNe = "तीव्र पीडा",
            remediesEn = listOf(
                "Lie down immediately and rest",
                "Apply heat to lower abdomen",
                "Do not ignore this — seek help"
            ),
            remediesNe = listOf(
                "तुरुन्तै सुत्नुहोस् र आराम गर्नुहोस्",
                "पेटको तल्लो भागमा तातो लगाउनुहोस्",
                "यसलाई बेवास्ता नगर्नुहोस् — सहायता लिनुहोस्"
            ),
            showWarning = true,
            warningEn = "If pain is unbearable or you are vomiting, please visit your nearest health post immediately.",
            warningNe = "यदि पीडा असह्य छ वा बान्ता भइरहेको छ भने कृपया तुरुन्तै नजिकको स्वास्थ्य चौकीमा जानुहोस्।"
        )
    )
}