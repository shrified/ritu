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
        ),
        Myth(
            mythEn = "You cannot enter the kitchen or temple during your period",
            mythNe = "महिनावारीमा भान्सा वा मन्दिर जान हुँदैन",
            truthEn = "Menstruation has no effect on food preparation or spiritual places. Excluding women from these spaces violates their rights and dignity.",
            truthNe = "महिनावारीले खाना पकाउने वा धार्मिक स्थलमा कुनै असर गर्दैन। महिलाहरूलाई यी ठाउँबाट बहिष्कार गर्नु उनीहरूको अधिकार र सम्मानको उल्लंघन हो।"
        ),
        Myth(
            mythEn = "Menstruating women bring bad luck or misfortune",
            mythNe = "महिनावारी भएकी महिलाले दुर्भाग्य ल्याउँछन्",
            truthEn = "This belief has no basis in fact. Menstruation is a healthy bodily function, not a source of bad luck. Such beliefs are used to control and isolate women.",
            truthNe = "यो विश्वासको कुनै तथ्यात्मक आधार छैन। महिनावारी एक स्वस्थ शारीरिक कार्य हो, दुर्भाग्यको स्रोत होइन। यस्ता विश्वासहरू महिलाहरूलाई नियन्त्रण गर्न र अलग राख्न प्रयोग गरिन्छ।"
        ),
        Myth(
            mythEn = "Girls should not attend school during their period",
            mythNe = "महिनावारीमा केटीहरू विद्यालय जानु हुँदैन",
            truthEn = "Menstruation does not affect a girl's ability to learn. Missing school during periods is a leading cause of girls falling behind and dropping out.",
            truthNe = "महिनावारीले केटीको सिक्ने क्षमतामा असर गर्दैन। महिनावारीमा विद्यालय नजानु केटीहरू पछाडि पर्ने र पढाई छाड्ने प्रमुख कारण हो।"
        ),
        Myth(
            mythEn = "Exercise and bathing are forbidden during menstruation",
            mythNe = "महिनावारीमा व्यायाम र नुहाउन निषेध छ",
            truthEn = "Bathing and gentle exercise are actually beneficial during menstruation. Denying women hygiene access increases the risk of infection.",
            truthNe = "नुहाउनु र हल्का व्यायाम वास्तवमा महिनावारीको समयमा लाभदायक हुन्छ। महिलाहरूलाई सरसफाइको पहुँचबाट वञ्चित गर्नाले संक्रमणको जोखिम बढाउँछ।"
        ),
        Myth(
            mythEn = "Chhaupadi is a religious requirement",
            mythNe = "छाउपडी धार्मिक आवश्यकता हो",
            truthEn = "No major religion requires chhaupadi. It is a social custom, not a divine command. Religious leaders and the Nepal Supreme Court have both spoken against it.",
            truthNe = "कुनै पनि प्रमुख धर्मले छाउपडी आवश्यक मान्दैन। यो एक सामाजिक प्रथा हो, दैवीय आदेश होइन। धार्मिक नेताहरू र नेपाल सर्वोच्च अदालत दुवैले यसको विरुद्धमा बोलेका छन्।"
        ),
        Myth(
            mythEn = "Irregular periods mean something is seriously wrong",
            mythNe = "अनियमित महिनावारी भनेको गम्भीर समस्या हो",
            truthEn = "Irregular periods are common and can be caused by stress, diet, weight changes or hormonal shifts. A healthcare provider can help identify if there is an underlying concern.",
            truthNe = "अनियमित महिनावारी सामान्य छ र तनाव, खानपान, तौल परिवर्तन वा हार्मोनल बदलावका कारण हुन सक्छ। स्वास्थ्यकर्मीले कुनै अन्तर्निहित समस्या छ कि छैन भनी पहिचान गर्न मद्दत गर्न सक्छन्।"
        )
    )
}