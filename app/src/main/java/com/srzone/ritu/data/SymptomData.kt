package com.srzone.ritu.data

object SymptomData {

    data class Symptom(
        val id: String,
        val labelEn: String,
        val labelNe: String,
        val descriptionEn: String,
        val descriptionNe: String,
        val causesEn: List<String>,
        val causesNe: List<String>,
        val remediesEn: List<String>,
        val remediesNe: List<String>,
        val seeDoctorEn: String,
        val seeDoctorNe: String
    )

    val symptoms = listOf(
        Symptom(
            id = "heavy_bleeding",
            labelEn = "Heavy Bleeding",
            labelNe = "अत्यधिक रक्तस्राव",
            descriptionEn = "Soaking more than one pad every 1-2 hours",
            descriptionNe = "हरेक १-२ घण्टामा एकभन्दा बढी प्याड भिज्नु",
            causesEn = listOf("Hormonal imbalance", "Stress", "Fibroids", "Thyroid issues"),
            causesNe = listOf("हर्मोनल असन्तुलन", "तनाव", "फाइब्रोइड", "थाइरोइड समस्या"),
            remediesEn = listOf(
                "Rest and avoid strenuous activity",
                "Eat iron-rich foods: spinach, lentils, meat",
                "Stay hydrated",
                "Track flow on your calendar"
            ),
            remediesNe = listOf(
                "आराम गर्नुहोस् र कठिन काम नगर्नुहोस्",
                "फलाम युक्त खाना खानुहोस्: पालुंगो, दाल, मासु",
                "पर्याप्त पानी पिउनुहोस्",
                "आफ्नो क्यालेन्डरमा प्रवाह ट्र्याक गर्नुहोस्"
            ),
            seeDoctorEn = "If you soak more than 2 pads per hour for more than 2 hours, or feel dizzy and weak.",
            seeDoctorNe = "यदि २ घण्टाभन्दा बढी समय प्रति घण्टा २ भन्दा बढी प्याड भिज्छ, वा चक्कर र कमजोरी महसुस हुन्छ।"
        ),
        Symptom(
            id = "no_period",
            labelEn = "Missed Period",
            labelNe = "महिनावारी नआउनु",
            descriptionEn = "Period is more than 7 days late",
            descriptionNe = "महिनावारी ७ दिनभन्दा बढी ढिला",
            causesEn = listOf("Stress", "Weight changes", "Hormonal imbalance", "Pregnancy", "Thyroid"),
            causesNe = listOf("तनाव", "तौल परिवर्तन", "हर्मोनल असन्तुलन", "गर्भावस्था", "थाइरोइड"),
            remediesEn = listOf(
                "Reduce stress through rest and light exercise",
                "Maintain regular meal times",
                "Avoid extreme dieting",
                "Track your cycle regularly"
            ),
            remediesNe = listOf(
                "आराम र हल्का व्यायामद्वारा तनाव कम गर्नुहोस्",
                "नियमित खाने समय कायम राख्नुहोस्",
                "अत्यधिक डाइटिङ नगर्नुहोस्",
                "आफ्नो चक्र नियमित रूपमा ट्र्याक गर्नुहोस्"
            ),
            seeDoctorEn = "If your period is more than 3 months late and you are not pregnant.",
            seeDoctorNe = "यदि तपाईं गर्भवती हुनुहुन्न र महिनावारी ३ महिनाभन्दा बढी ढिला छ।"
        ),
        Symptom(
            id = "irregular",
            labelEn = "Irregular Periods",
            labelNe = "अनियमित महिनावारी",
            descriptionEn = "Cycle length varies by more than 7 days each month",
            descriptionNe = "हर प्रत्येक महिना चक्रको अवधि ७ दिनभन्दा बढी फरक हुनु",
            causesEn = listOf("PCOS", "Stress", "Thyroid", "Puberty changes", "Weight issues"),
            causesNe = listOf("पीसीओएस", "तनाव", "थाइरोइड", "यौवन परिवर्तन", "तौल समस्या"),
            remediesEn = listOf(
                "Maintain consistent sleep schedule",
                "Eat balanced meals at regular times",
                "Reduce stress",
                "Light exercise like walking or yoga"
            ),
            remediesNe = listOf(
                "नियमित सुत्ने तालिका कायम राख्नुहोस्",
                "नियमित समयमा सन्तुलित खाना खानुहोस्",
                "तनाव कम गर्नुहोस्",
                "हिँडाइ वा योग जस्तो हल्का व्यायाम गर्नुहोस्"
            ),
            seeDoctorEn = "If periods have been irregular for more than 3 consecutive months.",
            seeDoctorNe = "यदि ३ लगातार महिनाभन्दा बढी समय महिनावारी अनियमित छ।"
        ),
        Symptom(
            id = "discharge",
            labelEn = "Unusual Discharge",
            labelNe = "असामान्य स्राव",
            descriptionEn = "Discharge that is unusual in color, smell or amount",
            descriptionNe = "रङ, गन्ध वा मात्रामा असामान्य स्राव",
            causesEn = listOf("Infection", "Hormonal changes", "Poor hygiene", "Normal ovulation discharge"),
            causesNe = listOf("संक्रमण", "हर्मोनल परिवर्तन", "खराब स्वच्छता", "सामान्य डिम्बोत्सर्जन स्राव"),
            remediesEn = listOf(
                "Maintain proper hygiene",
                "Wear clean cotton underwear",
                "Avoid scented soaps in the genital area",
                "Change pads/underwear regularly"
            ),
            remediesNe = listOf(
                "उचित स्वच्छता कायम राख्नुहोस्",
                "सफा सूती अन्डरवियर लगाउनुहोस्",
                "जननांग क्षेत्रमा सुगन्धित साबुन प्रयोग नगर्नुहोस्",
                "प्याड/अन्डरवियर नियमित रूपमा फेर्नुहोस्"
            ),
            seeDoctorEn = "If discharge is yellow, green, or has a strong unpleasant smell.",
            seeDoctorNe = "यदि स्राव पहेँलो, हरियो छ वा तीव्र अप्रिय गन्ध छ।"
        )
    )
}