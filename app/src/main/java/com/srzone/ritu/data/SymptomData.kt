package com.srzone.ritu.Data

object SymptomData {

    data class SecondarySymptom(
        val id: String,
        val labelEn: String,
        val labelNe: String
    )

    data class Symptom(
        val id: String,
        val labelEn: String,
        val labelNe: String,
        val descriptionEn: String,
        val descriptionNe: String,
        val secondarySymptoms: List<SecondarySymptom>,
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
            descriptionEn = "Soaking more than one pad every 1–2 hours",
            descriptionNe = "हरेक १–२ घण्टामा एकभन्दा बढी प्याड भिज्नु",
            secondarySymptoms = listOf(
                SecondarySymptom("clots",     "Passing clots",        "थक्का जानु"),
                SecondarySymptom("dizzy",     "Dizziness / fainting", "चक्कर / बेहोशी"),
                SecondarySymptom("fatigue",   "Extreme fatigue",      "अत्यधिक थकान"),
                SecondarySymptom("pale",      "Pale skin",            "छाला फिक्का हुनु"),
                SecondarySymptom("backpain",  "Lower back pain",      "ढाडको तल्लो भाग दुख्नु")
            ),
            causesEn = listOf("Hormonal imbalance", "Stress", "Fibroids", "Thyroid issues"),
            causesNe = listOf("हर्मोनल असन्तुलन", "तनाव", "फाइब्रोइड", "थाइरोइड समस्या"),
            remediesEn = listOf(
                "Rest and avoid strenuous activity",
                "Eat iron-rich foods: spinach, lentils, meat",
                "Stay well hydrated",
                "Track your flow on the calendar"
            ),
            remediesNe = listOf(
                "आराम गर्नुहोस् र कठिन काम नगर्नुहोस्",
                "फलाम युक्त खाना खानुहोस्: पालुंगो, दाल, मासु",
                "पर्याप्त पानी पिउनुहोस्",
                "आफ्नो क्यालेन्डरमा प्रवाह ट्र्याक गर्नुहोस्"
            ),
            seeDoctorEn = "If you soak more than 2 pads per hour for over 2 hours, or feel dizzy and very weak.",
            seeDoctorNe = "यदि २ घण्टाभन्दा बढी समय प्रति घण्टा २ भन्दा बढी प्याड भिज्छ, वा चक्कर र कमजोरी महसुस हुन्छ।"
        ),
        Symptom(
            id = "cramps",
            labelEn = "Cramps",
            labelNe = "पेट दुखाइ",
            descriptionEn = "Pain or cramping in the lower abdomen during or around your period",
            descriptionNe = "महिनावारीको समयमा वा वरिपरि पेटको तल्लो भागमा दुखाइ",
            secondarySymptoms = listOf(
                SecondarySymptom("nausea",    "Nausea",               "वाकवाकी"),
                SecondarySymptom("vomiting",  "Vomiting",             "बान्ता"),
                SecondarySymptom("backpain",  "Lower back pain",      "ढाडको तल्लो भाग दुख्नु"),
                SecondarySymptom("headache",  "Headache",             "टाउको दुख्नु"),
                SecondarySymptom("diarrhea",  "Diarrhea",             "पखाला")
            ),
            causesEn = listOf("Uterine contractions", "Endometriosis", "Stress", "Poor diet"),
            causesNe = listOf("पाठेघरको संकुचन", "एन्डोमेट्रियोसिस", "तनाव", "खराब आहार"),
            remediesEn = listOf(
                "Apply a hot water bottle to your lower abdomen",
                "Drink ginger or cinnamon tea",
                "Light walking or gentle stretching",
                "Rest in a comfortable position",
                "Ibuprofen with food if available"
            ),
            remediesNe = listOf(
                "पेटको तल्लो भागमा तातो पानीको बोतल राख्नुहोस्",
                "अदुवा वा दालचिनीको चिया पिउनुहोस्",
                "हल्का हिँडाइ वा स्ट्रेचिङ गर्नुहोस्",
                "आरामदायक स्थितिमा आराम गर्नुहोस्",
                "उपलब्ध भएमा खाना खाएपछि आइबुप्रोफेन लिनुहोस्"
            ),
            seeDoctorEn = "If cramps are severe enough to prevent daily activity, or pain continues after your period ends.",
            seeDoctorNe = "यदि पीडाले दैनिक काम गर्न नसक्ने अवस्था छ, वा महिनावारी सकिएपछि पनि पीडा जारी छ।"
        ),
        Symptom(
            id = "irregular",
            labelEn = "Irregular Periods",
            labelNe = "अनियमित महिनावारी",
            descriptionEn = "Cycle length varies by more than 7 days each month",
            descriptionNe = "हरेक महिना चक्रको अवधि ७ दिनभन्दा बढी फरक हुनु",
            secondarySymptoms = listOf(
                SecondarySymptom("weight",    "Unexplained weight change", "अनायास तौल परिवर्तन"),
                SecondarySymptom("acne",      "Acne / skin changes",       "मुहासो / छाला परिवर्तन"),
                SecondarySymptom("hairloss",  "Hair thinning",             "कपाल पातलो हुनु"),
                SecondarySymptom("stress",    "High stress lately",        "हालै उच्च तनाव"),
                SecondarySymptom("sleep",     "Sleep problems",            "निद्रा समस्या")
            ),
            causesEn = listOf("PCOS", "Stress", "Thyroid", "Puberty changes", "Weight issues"),
            causesNe = listOf("पीसीओएस", "तनाव", "थाइरोइड", "यौवन परिवर्तन", "तौल समस्या"),
            remediesEn = listOf(
                "Maintain a consistent sleep schedule",
                "Eat balanced meals at regular times",
                "Reduce stress through rest or light exercise",
                "Light exercise like walking or yoga"
            ),
            remediesNe = listOf(
                "नियमित सुत्ने तालिका कायम राख्नुहोस्",
                "नियमित समयमा सन्तुलित खाना खानुहोस्",
                "आराम वा हल्का व्यायामद्वारा तनाव कम गर्नुहोस्",
                "हिँडाइ वा योग जस्तो हल्का व्यायाम गर्नुहोस्"
            ),
            seeDoctorEn = "If periods have been irregular for more than 3 consecutive months.",
            seeDoctorNe = "यदि ३ लगातार महिनाभन्दा बढी समय महिनावारी अनियमित छ।"
        ),
        Symptom(
            id = "no_period",
            labelEn = "Missed Period",
            labelNe = "महिनावारी नआउनु",
            descriptionEn = "Period is more than 7 days late",
            descriptionNe = "महिनावारी ७ दिनभन्दा बढी ढिला",
            secondarySymptoms = listOf(
                SecondarySymptom("stress",    "High stress lately",        "हालै उच्च तनाव"),
                SecondarySymptom("weight",    "Significant weight change",  "उल्लेखनीय तौल परिवर्तन"),
                SecondarySymptom("nausea",    "Nausea / vomiting",         "वाकवाकी / बान्ता"),
                SecondarySymptom("discharge", "Unusual discharge",         "असामान्य स्राव"),
                SecondarySymptom("fatigue",   "Unusual fatigue",           "असामान्य थकान")
            ),
            causesEn = listOf("Stress", "Weight changes", "Hormonal imbalance", "Pregnancy", "Thyroid"),
            causesNe = listOf("तनाव", "तौल परिवर्तन", "हर्मोनल असन्तुलन", "गर्भावस्था", "थाइरोइड"),
            remediesEn = listOf(
                "Reduce stress through rest and light exercise",
                "Maintain regular meal times",
                "Avoid extreme dieting",
                "Track your cycle regularly in the app"
            ),
            remediesNe = listOf(
                "आराम र हल्का व्यायामद्वारा तनाव कम गर्नुहोस्",
                "नियमित खाने समय कायम राख्नुहोस्",
                "अत्यधिक डाइटिङ नगर्नुहोस्",
                "एपमा आफ्नो चक्र नियमित रूपमा ट्र्याक गर्नुहोस्"
            ),
            seeDoctorEn = "If your period is more than 3 months late and you are not pregnant.",
            seeDoctorNe = "यदि तपाईं गर्भवती हुनुहुन्न र महिनावारी ३ महिनाभन्दा बढी ढिला छ।"
        ),
        Symptom(
            id = "discharge",
            labelEn = "Unusual Discharge",
            labelNe = "असामान्य स्राव",
            descriptionEn = "Discharge unusual in colour, smell or amount",
            descriptionNe = "रङ, गन्ध वा मात्रामा असामान्य स्राव",
            secondarySymptoms = listOf(
                SecondarySymptom("itching",   "Itching or burning",   "खुजली वा जलन"),
                SecondarySymptom("odor",      "Strong odor",          "तीव्र गन्ध"),
                SecondarySymptom("color",     "Yellow or green color","पहेँलो वा हरियो रङ"),
                SecondarySymptom("pain",      "Pain during urination","पिसाब गर्दा दुख्नु"),
                SecondarySymptom("soreness",  "Soreness or swelling", "दुखाइ वा सुन्निनु")
            ),
            causesEn = listOf("Infection", "Hormonal changes", "Poor hygiene", "Normal ovulation discharge"),
            causesNe = listOf("संक्रमण", "हर्मोनल परिवर्तन", "खराब स्वच्छता", "सामान्य डिम्बोत्सर्जन स्राव"),
            remediesEn = listOf(
                "Maintain proper hygiene — wash gently with water only",
                "Wear clean, dry cotton underwear",
                "Avoid scented soaps or sprays in the genital area",
                "Change pads or underwear regularly"
            ),
            remediesNe = listOf(
                "उचित स्वच्छता कायम राख्नुहोस् — केवल पानीले बिस्तारै धुनुहोस्",
                "सफा, सुख्खा सूती अन्डरवियर लगाउनुहोस्",
                "जननांग क्षेत्रमा सुगन्धित साबुन वा स्प्रे प्रयोग नगर्नुहोस्",
                "प्याड वा अन्डरवियर नियमित रूपमा फेर्नुहोस्"
            ),
            seeDoctorEn = "If discharge is yellow, green, or has a strong unpleasant smell, or if you feel pain or burning.",
            seeDoctorNe = "यदि स्राव पहेँलो, हरियो छ वा तीव्र अप्रिय गन्ध छ, वा दुखाइ वा जलन महसुस हुन्छ।"
        )
    )
}