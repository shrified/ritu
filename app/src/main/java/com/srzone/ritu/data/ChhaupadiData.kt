package com.srzone.ritu.data

object ChhaupadiData {
        val titleEn = "The Truth About Chhaupadi"
        val titleNe = "छाउपडीको बारेमा सत्य"

        data class Myth(
            val mythEn: String,
            val mythNe: String,
            val truthEn: String,
            val truthNe: String
        )

        val myths = listOf(
            Myth(
                mythEn = "Menstruating women are impure",
                mythNe = "महिनावारी भएकी महिला अशुद्ध हुन्छन्",
                truthEn = "Menstruation is a completely natural biological process. It is not a sign of impurity. Every woman menstruates.",
                truthNe = "महिनावारी एक पूर्णतः प्राकृतिक जैविक प्रक्रिया हो। यो अशुद्धताको संकेत होइन। हरेक महिलालाई महिनावारी हुन्छ।"
            ),
            Myth(
                mythEn = "You must sleep outside during your period",
                mythNe = "महिनावारीमा बाहिर सुत्नुपर्छ",
                truthEn = "Chhaupadi was criminalized in Nepal in 2017. Sleeping in chhaupadi huts is dangerous and has caused deaths from cold, snake bites and suffocation.",
                truthNe = "नेपालमा सन् २०१७ मा छाउपडीलाई कानुनी अपराध घोषित गरियो। छाउगोठमा सुत्नु खतरनाक छ र चिसो, सर्पदंश र दमका कारण मृत्यु भएका छन्।"
            ),
            Myth(
                mythEn = "You cannot touch food, water or family members",
                mythNe = "खाना, पानी वा परिवारलाई छुन हुँदैन",
                truthEn = "There is no scientific basis for this. Menstruation does not contaminate food or water. This practice causes malnutrition and dehydration.",
                truthNe = "यसको कुनै वैज्ञानिक आधार छैन। महिनावारीले खाना वा पानी दूषित गर्दैन। यो प्रथाले कुपोषण र निर्जलीकरण निम्त्याउँछ।"
            ),
            Myth(
                mythEn = "Period blood is dirty or toxic",
                mythNe = "महिनावारीको रगत फोहोर वा विषाक्त हुन्छ",
                truthEn = "Period blood is simply the uterine lining shedding. It is not toxic or dirty. It is a sign that your body is healthy and functioning normally.",
                truthNe = "महिनावारीको रगत केवल पाठेघरको भित्री तह हो। यो विषाक्त वा फोहोर होइन। यो संकेत हो कि तपाईंको शरीर स्वस्थ र सामान्य रूपमा काम गरिरहेको छ।"
            )
        )
}