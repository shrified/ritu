package com.srzone.ritu.Utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import com.srzone.ritu.R
import java.io.IOException
import java.util.Locale
import kotlin.Any
import kotlin.Exception
import kotlin.Int
import kotlin.String

object Utils {
    fun setStatusBarColor(i: Int, activity: Activity) {
        val window = activity.getWindow()
        window.addFlags(Int.Companion.MIN_VALUE)
        window.setStatusBarColor(ContextCompat.getColor(activity, i))
    }

    fun getStringFromObj(obj: Any?): String? {
        if (obj != null) {
            return obj.toString()
        }
        return null
    }

    fun getBoolFromObj(obj: Any?): Boolean {
        if (obj is Boolean) return obj
        if (obj is java.lang.Boolean) return obj.booleanValue()
        return false
    }

    fun makeTransparentStatusBar(activity: Activity) {
        val window = activity.getWindow()
        window.getDecorView().setSystemUiVisibility(1280)
        window.setStatusBarColor(0)
    }

    fun setFullScreen(activity: Activity) {
        activity.getWindow().setFlags(512, 512)
    }

    fun readAssetFile(context: Context, str: String): MutableList<HashMap<String?, Any?>?>? {
        val str2: String?
        try {
            return JsonHelper.getJsonData(context, str)
        } catch (unused: Exception) {
            if (str.endsWith("_c.json")) {
                str2 = "en_c.json"
            } else {
                str2 = if (str.endsWith("_g.json")) "en_g.json" else "en.json"
            }
            try {
                return JsonHelper.getJsonData(context, str2)
            } catch (unused2: IOException) {
                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show()
                return null
            }
        }
    }

    fun getResId(str: String, activity: Activity): Int {
        val str2 = str
        var identifier =
            activity.getResources().getIdentifier(str2, "drawable", activity.getPackageName())
        if (identifier == 0) {
            str2.replace("ı", "i")
            identifier =
                activity.getResources().getIdentifier(str2, "drawable", activity.getPackageName())
        }
        Log.e("MYTAG", "ErrorNo: getResId drawable:" + str2 + " : " + identifier)
        return identifier
    }

    fun htmlToText(str: String): String {
        return HtmlCompat.fromHtml(str, 0).toString()
    }

    fun lowerUnder(str: String): String {
        var str = str
        if (str.endsWith("?")) {
            str = str.substring(0, str.length - 1)
        }
        val replace = str.lowercase(Locale.getDefault()).replace(" ", "_").replace("&", "and")
            .replace("-", "_").replace(",", "").replace("'", "")
        if (startsWithDigit(replace)) {
            return "_" + replace
        }
        return replace
    }

    private fun startsWithDigit(str: String?): kotlin.Boolean {
        if (str == null || str.isEmpty()) {
            return false
        }
        return Character.isDigit(str.get(0))
    }

    fun setButtonTint(appCompatButton: AppCompatButton, i: Int) {
        val wrap = DrawableCompat.wrap(appCompatButton.getBackground())
        DrawableCompat.setTint(wrap, ContextCompat.getColor(appCompatButton.getContext(), i))
        appCompatButton.setBackgroundDrawable(wrap)
    }

    fun setTint(linearLayout: LinearLayout, i: Int) {
        linearLayout.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    linearLayout.getContext(),
                    i
                )
            )
        )
    }

    fun setButtonTint(button: Button, i: Int) {
        button.setBackgroundTintList(ContextCompat.getColorStateList(button.getContext(), i))
    }

    fun darkStatusBarIcons(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            activity.getWindow().getDecorView().setSystemUiVisibility(8192)
        }
    }

    fun hideKeyboard(activity: Activity) {
        try {
            (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                activity.getCurrentFocus()!!.getWindowToken(),
                0
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setImage(s: String): Int {
        when (s) {
            "how_do_i_know_when_i_am_ovulating" -> return R.drawable.how_do_i_know_when_i_am_ovulating
            "what_are_the_signs_of_ovulation" -> return R.drawable.what_are_the_signs_of_ovulation
            "how_long_does_ovulation_last" -> return R.drawable.how_long_does_ovulation_last

            "what_is_the_best_way_to_track_ovulation" -> return R.drawable.what_is_the_best_way_to_track_ovulation

            "can_i_still_ovulate_if_i_have_irregular_periods" -> return R.drawable.can_i_still_ovulate_if_i_have_irregular_periods

            "what_is_the_most_fertile_time_during_ovulation" -> return R.drawable.what_is_the_most_fertile_time_during_ovulation

            "can_i_get_pregnant_if_i_have_sex_before_ovulation" -> return R.drawable.can_i_get_pregnant_if_i_have_sex_before_ovulation

            "how_can_i_increase_my_chances_of_ovulation" -> return R.drawable.how_can_i_increase_my_chances_of_ovulation

            "what_are_the_symptoms_of_ovulation" -> return R.drawable.what_are_the_symptoms_of_ovulation

            "can_i_ovulate_without_having_a_period" -> return R.drawable.can_i_ovulate_without_having_a_period

            "can_stress_affect_ovulation" -> return R.drawable.can_stress_affect_ovulation

            "how_does_age_affect_ovulation" -> return R.drawable.how_does_age_affect_ovulation

            "can_i_ovulate_twice_in_one_cycle" -> return R.drawable.can_i_ovulate_twice_in_one_cycle

            "can_i_get_pregnant_during_early_ovulation" -> return R.drawable.can_i_get_pregnant_during_early_ovulation

            "can_i_still_ovulate_with_low_progesterone" -> return R.drawable.can_i_still_ovulate_with_low_progesterone

            "can_i_ovulate_while_breastfeeding" -> return R.drawable.can_i_ovulate_while_breastfeeding

            "can_i_get_pregnant_during_late_ovulation" -> return R.drawable.can_i_get_pregnant_during_late_ovulation

            "can_i_ovulate_without_cervical_mucus" -> return R.drawable.can_i_ovulate_without_cervical_mucus

            "what_are_the_causes_of_ovulation_pain" -> return R.drawable.what_are_the_causes_of_ovulation_pain

            "how_does_birth_control_affect_ovulation" -> return R.drawable.how_does_birth_control_affect_ovulation

            "can_i_get_pregnant_during_ovulation" -> return R.drawable.can_i_get_pregnant_during_ovulation

            "how_to_improve_fertility" -> return R.drawable.how_to_improve_fertility

            "what_are_the_signs_of_fertility_in_females" -> return R.drawable.what_are_the_signs_of_fertility_in_females

            "how_to_test_fertility_in_females" -> return R.drawable.how_to_test_fertility_in_females

            "what_is_the_normal_fertility_rate" -> return R.drawable.what_is_the_normal_fertility_rate

            "what_are_the_causes_of_infertility_in_females" -> return R.drawable.what_are_the_causes_of_infertility_in_females

            "can_stress_affect_fertility" -> return R.drawable.can_stress_affect_fertility

            "can_diet_affect_fertility" -> return R.drawable.can_diet_affect_fertility

            "can_lifestyle_affect_fertility" -> return R.drawable.can_lifestyle_affect_fertility

            "how_to_boost_female_fertility_naturally" -> return R.drawable.how_to_boost_female_fertility_naturally

            "how_to_track_ovulation_for_better_fertility" -> return R.drawable.how_to_track_ovulation_for_better_fertility

            "how_to_improve_fertility_after_age_35" -> return R.drawable.how_to_improve_fertility_after_age_35

            "can_i_get_pregnant_with_one_fallopian_tube" -> return R.drawable.can_i_get_pregnant_with_one_fallopian_tube

            "how_to_improve_fertility_with_low_sperm_count" -> return R.drawable.how_to_improve_fertility_with_low_sperm_count

            "how_to_improve_fertility_with_irregular_periods" -> return R.drawable.how_to_improve_fertility_with_irregular_periods

            "how_to_improve_fertility_with_thyroid_problems" -> return R.drawable.how_to_improve_fertility_with_thyroid_problems

            "can_i_get_pregnant_with_irregular_periods" -> return R.drawable.can_i_get_pregnant_with_irregular_periods

            "can_i_get_pregnant_with_low_sperm_motility" -> return R.drawable.can_i_get_pregnant_with_low_sperm_motility

            "what_is_a_normal_period" -> return R.drawable.what_is_a_normal_period

            "what_is_a_heavy_period" -> return R.drawable.what_is_a_heavy_period

            "what_is_a_light_period" -> return R.drawable.what_is_a_light_period

            "what_are_the_signs_of_an_irregular_period" -> return R.drawable.what_are_the_signs_of_an_irregular_period

            "how_can_i_make_my_period_come_faster" -> return R.drawable.how_can_i_make_my_period_come_faster

            "how_can_i_delay_my_period" -> return R.drawable.how_can_i_delay_my_period

            "how_can_i_relieve_pms_symptoms" -> return R.drawable.how_can_i_relieve_pms_symptoms

            "what_are_the_symptoms_of_pms" -> return R.drawable.what_are_the_symptoms_of_pms

            "how_can_i_manage_period_cramps" -> return R.drawable.how_can_i_manage_period_cramps

            "can_exercise_affect_my_period" -> return R.drawable.can_exercise_affect_my_period

            "can_diet_affect_my_period" -> return R.drawable.can_diet_affect_my_period

            "can_stress_affect_my_period" -> return R.drawable.can_stress_affect_my_period

            "how_can_i_manage_heavy_bleeding_during_my_period" -> return R.drawable.how_can_i_manage_heavy_bleeding_during_my_period

            "how_can_i_track_my_period" -> return R.drawable.how_can_i_track_my_period

            "how_can_i_calculate_my_period_cycle" -> return R.drawable.how_can_i_calculate_my_period_cycle

            "how_can_i_use_birth_control_to_regulate_my_period" -> return R.drawable.how_can_i_use_birth_control_to_regulate_my_period

            "how_long_is_a_menstrual_cycle" -> return R.drawable.how_long_is_a_menstrual_cycle

            "what_is_a_normal_menstrual_cycle" -> return R.drawable.what_is_a_normal_menstrual_cycle

            "what_is_an_irregular_menstrual_cycle" -> return R.drawable.what_is_an_irregular_menstrual_cycle

            "how_can_i_track_my_ovulation" -> return R.drawable.how_can_i_track_my_ovulation

            "can_i_get_pregnant_during_my_menstrual_cycle" -> return R.drawable.can_i_get_pregnant_during_my_menstrual_cycle

            "can_i_get_pregnancan_stress_affect_my_menstrual_cyclet_during_my_menstrual_cycle" -> return R.drawable.can_stress_affect_my_menstrual_cycle

            "can_diet_affect_my_menstrual_cycle" -> return R.drawable.can_diet_affect_my_menstrual_cycle

            "can_exercise_affect_my_menstrual_cycle" -> return R.drawable.can_exercise_affect_my_menstrual_cycle

            "how_can_i_manage_menstrual_cramps" -> return R.drawable.how_can_i_manage_menstrual_cramps

            "how_can_i_manage_heavy_bleeding_during_my_menstrual_cycle" -> return R.drawable.how_can_i_manage_heavy_bleeding_during_my_menstrual_cycle

            "what_is_ovulation_and_how_does_it_affect_fertility" -> return R.drawable.what_is_ovulation_and_how_does_it_affect_fertility

            "how_long_is_a_typical_menstrual_cycle" -> return R.drawable.can_i_get_pregnant_during_my_menstrual_cycle

            "what_is_the_menstrual_phase_of_the_menstrual_cycle" -> return R.drawable.what_is_the_menstrual_phase_of_the_menstrual_cycle

            "what_is_the_fertile_window" -> return R.drawable.what_is_the_fertile_window

            "what_are_the_best_ways_to_increase_fertility" -> return R.drawable.what_are_the_best_ways_to_increase_fertility

            "how_does_age_affect_fertility" -> return R.drawable.how_does_age_affect_fertility

            "can_medications_affect_fertility" -> return R.drawable.can_medications_affect_fertility

            "what_are_the_best_natural_ways_to_improve_fertility" -> return R.drawable.what_are_the_best_natural_ways_to_improve_fertility

            "how_can_i_increase_my_chances_of_getting_pregnant" -> return R.drawable.how_can_i_increase_my_chances_of_getting_pregnant

            "can_i_get_pregnant_if_i_have_sex_on_my_period" -> return R.drawable.can_i_get_pregnant_if_i_have_sex_on_my_period

            "what_is_secondary_infertility" -> return R.drawable.what_is_secondary_infertility

            "what_is_unexplained_infertility" -> return R.drawable.what_is_unexplained_infertility

            "how_can_i_manage_pms_symptoms" -> return R.drawable.how_can_i_manage_pms_symptoms

            "what_is_infertility_and_what_are_the_causes" -> return R.drawable.what_is_infertility_and_what_are_the_causes

            "what_are_the_treatment_options_for_infertility" -> return R.drawable.what_are_the_treatment_options_for_infertility

            "what_are_the_side_effects_of_fertility_medications" -> return R.drawable.what_are_the_side_effects_of_fertility_medications

            "how_can_i_prepare_for_a_healthy_pregnancy" -> return R.drawable.how_can_i_prepare_for_a_healthy_pregnancy

            "what_foods_reduce_period_pain" -> return R.drawable.what_foods_reduce_period_pain

            "what_makes_period_cramps_worse" -> return R.drawable.what_makes_period_cramps_worse

            "what_drink_is_good_for_cramps" -> return R.drawable.what_drink_is_good_for_cramps

            "what_is_the_main_cause_of_cramp" -> return R.drawable.what_is_the_main_cause_of_cramp

            "why_do_my_legs_hurt_on_my_period" -> return R.drawable.why_do_my_legs_hurt_on_my_period

            "what_foods_cause_heavy_periods" -> return R.drawable.what_foods_cause_heavy_periods

            "what_is_emergency_contraception" -> return R.drawable.what_is_emergency_contraception

            "what_can_i_do_to_maintain_my_reproductive_health" -> return R.drawable.what_can_i_do_to_maintain_my_reproductive_health

            "how_can_i_improve_my_female_reproductive_system" -> return R.drawable.how_can_i_improve_my_female_reproductive_system

            "how_many_days_after_my_period_can_i_get_pregnant_calculator" -> return R.drawable.how_many_days_after_my_period_can_i_get_pregnant_calculator

            "how_effective_are_natural_family_planning_methods" -> return R.drawable.how_effective_are_natural_family_planning_methods

            "can_i_get_pregnant_right_after_my_period_ends" -> return R.drawable.can_i_get_pregnant_right_after_my_period_ends

            "is_it_safe_to_take_medicine_for_improve_fertility" -> return R.drawable.is_it_safe_to_take_medicine_for_improve_fertility

            "is_it_good_to_take_medicine_to_conceive" -> return R.drawable.is_it_good_to_take_medicine_to_conceive

            "what_are_the_side_effects_of_fertility_pills" -> return R.drawable.what_are_the_side_effects_of_fertility_pills

            "what_are_the_symptoms_of_poor_egg_quality" -> return R.drawable.what_are_the_symptoms_of_poor_egg_quality

            "how_can_i_increase_my_ovaries_eggs_naturally" -> return R.drawable.how_can_i_increase_my_ovaries_eggs_naturally

            "increase_male_fertility" -> return R.drawable.increase_male_fertility

            "transition_to_fertility" -> return R.drawable.transition_to_fertility

            "female_fertility_check" -> return R.drawable.female_fertility_check

            "_3_causes_of_infertility" -> return R.drawable._3_causes_of_infertility

            "foods_for_sperm_count" -> return R.drawable.foods_for_sperm_count

            "male_infertility_cause" -> return R.drawable.male_infertility_cause

            "pregnancy_in_infertile_women" -> return R.drawable.pregnancy_in_infertile_women

            "post_sex_precautions" -> return R.drawable.post_sex_precautions

            "best_days_for_sex" -> return R.drawable.best_days_for_sex

            "frequency_of_sex" -> return R.drawable.frequency_of_sex

            "sex_protection" -> return R.drawable.sex_protection

            "lifelong_intimacy" -> return R.drawable.lifelong_intimacy

            "intimacy_for_men" -> return R.drawable.intimacy_for_men

            "power_of_eye_contact" -> return R.drawable.power_of_eye_contact

            "stress_and_delayed_period" -> return R.drawable.stress_and_delayed_period

            "cramps_relief" -> return R.drawable.cramps_relief

            "stress_period_overview" -> return R.drawable.stress_period_overview

            "stress_and_fertility_connection" -> return R.drawable.stress_and_fertility_connection

            "stress_and_fertility" -> return R.drawable.stress_and_fertility

            "emotional_impact_on_ovulation" -> return R.drawable.emotional_impact_on_ovulation

            "stress_and_ovulation_delay" -> return R.drawable.stress_and_ovulation_delay

            "managing_stressful_periods" -> return R.drawable.managing_stressful_periods

            "emotions_and_ovulation" -> return R.drawable.emotions_and_ovulation

            "coping_with_stressful_periods" -> return R.drawable.coping_with_stressful_periods

            "cramps_without_period" -> return R.drawable.cramps_without_period

            "damaging_egg_quality" -> return R.drawable.damaging_egg_quality

            "relaxation_for_pregnancy" -> return R.drawable.relaxation_for_pregnancy

            "igniting_passion" -> return R.drawable.igniting_passion

            "secrets_to_lasting_closeness" -> return R.drawable.secrets_to_lasting_closeness

            "intimacy_hacks" -> return R.drawable.intimacy_hacks

            "role_of_touch" -> return R.drawable.role_of_touch

            "types_of_intimacy" -> return R.drawable.types_of_intimacy

            "art_of_intimacy" -> return R.drawable.art_of_intimacy

            "sparking_desire" -> return R.drawable.sparking_desire

            "anxiety_and_fertility" -> return R.drawable.anxiety_and_fertility

            "impact_of_sexual_activity_on_health_and_well_being" -> return R.drawable.impact_of_sexual_activity_on_health_and_well_being

            "the_5_ps_of_sexual_health" -> return R.drawable.the_5_ps_of_sexual_health

            "food_for_period_cramp_relief" -> return R.drawable.food_for_period_cramp_relief

            "period_pain_without_period" -> return R.drawable.period_pain_without_period

            "juice_during_periods" -> return R.drawable.juice_during_periods

            "fast_home_period_pain_relief" -> return R.drawable.fast_home_period_pain_relief

            "menstrual_cramp_causes" -> return R.drawable.menstrual_cramp_causes

            "premature_ejaculation_in_men" -> return R.drawable.premature_ejaculation_in_men

            "reduce_heavy_bleeding" -> return R.drawable.reduce_heavy_bleeding

            "stress_and_heavy_periods" -> return R.drawable.stress_and_heavy_periods

            "stress_and_egg_quality" -> return R.drawable.stress_and_egg_quality

            "periods_and_emotional_changes" -> return R.drawable.periods_and_emotional_changes

            "crying_in_ovulation" -> return R.drawable.crying_in_ovulation

            "period_and_depression" -> return R.drawable.period_and_depression

            "periods_and_mental_health" -> return R.drawable.periods_and_mental_health

            "ovulation_depression" -> return R.drawable.ovulation_depression

            "pms_depression" -> return R.drawable.pms_depression

            "high_bleeding_in_pregnancy" -> return R.drawable.high_bleeding_in_pregnancy

            "high_bleeding_and_hormonal_imbalance" -> return R.drawable.high_bleeding_and_hormonal_imbalance

            "home_sperm_count_check" -> return R.drawable.home_sperm_count_check

            "fertility_foods" -> return R.drawable.fertility_foods

            "aging_and_sexual_health" -> return R.drawable.aging_and_sexual_health

            "common_sexual_health_problems" -> return R.drawable.common_sexual_health_problems

            "improve_sexual_intimacy_and_communication" -> return R.drawable.improve_sexual_intimacy_and_communication

            "mental_and_sexual_health" -> return R.drawable.mental_and_sexual_health

            "maintaining_sexual_health" -> return R.drawable.maintaining_sexual_health

            "enhance_sexual_pleasure" -> return R.drawable.enhance_sexual_pleasure

            "causes_of_weak_erection" -> return R.drawable.causes_of_weak_erection

            "thick_and_strong_sperm" -> return R.drawable.thick_and_strong_sperm

            "sex_on_period" -> return R.drawable.sex_on_period

            "ovulation_and_sex_sensation" -> return R.drawable.ovulation_and_sex_sensation

            "preventing_high_bleeding" -> return R.drawable.preventing_high_bleeding

            "normal_heavy_bleeding" -> return R.drawable.normal_heavy_bleeding

            "stopping_bleeding" -> return R.drawable.stopping_bleeding

            "guys_role_during_periods" -> return R.drawable.guys_role_during_periods

            "daily_sex_and_pregnancy" -> return R.drawable.daily_sex_and_pregnancy

            "ovulation_on_birth_control" -> return R.drawable.ovulation_on_birth_control

            "birth_control_side_effects" -> return R.drawable.birth_control_side_effects

            "weak_penis_cause" -> return R.drawable.weak_penis_cause

            "strong_sex_power" -> return R.drawable.strong_sex_power

            "contents_of_birth_control" -> return R.drawable.contents_of_birth_control

            "allergies_and_irritation_from_menstrual_products" -> return R.drawable.allergies_and_irritation_from_menstrual_products

            "overnight_use_of_menstrual_products" -> return R.drawable.overnight_use_of_menstrual_products

            "natural_birth_control" -> return R.drawable.natural_birth_control

            "sex_frequency_and_fertility" -> return R.drawable.sex_frequency_and_fertility

            "period_blood_and_men" -> return R.drawable.period_blood_and_men

            "husbands_support_during_periods" -> return R.drawable.husbands_support_during_periods

            "risky_sex_period" -> return R.drawable.risky_sex_period

            "safety_of_menstrual_cups" -> return R.drawable.safety_of_menstrual_cups

            "stress_mental_health_and_fertility" -> return R.drawable.stress_mental_health_and_fertility

            "age_and_fertility" -> return R.drawable.age_and_fertility

            "menstrual_product_safety" -> return R.drawable.menstrual_product_safety

            "moodiness_and_fatigue_on_birth_control" -> return R.drawable.moodiness_and_fatigue_on_birth_control

            "birth_control_and_pregnancy" -> return R.drawable.birth_control_and_pregnancy

            "foods_for_female_fertility" -> return R.drawable.foods_for_female_fertility

            "fertility_boosting_fruits" -> return R.drawable.fertility_boosting_fruits

            "avoiding_ovulation_disruptors" -> return R.drawable.avoiding_ovulation_disruptors

            "improving_male_fertility_and_sperm_health" -> return R.drawable.improving_male_fertility_and_sperm_health

            "best_menstrual_products_for_heavy_periods" -> return R.drawable.best_menstrual_products_for_heavy_periods

            "how_menstrual_pads_work" -> return R.drawable.how_menstrual_pads_work

            "menstrual_cups_for_teenagers" -> return R.drawable.menstrual_cups_for_teenagers

            "correct_usage_of_tampons" -> return R.drawable.correct_usage_of_tampons

            "choosing_the_right_menstrual_product" -> return R.drawable.choosing_the_right_menstrual_product

            "nourishing_menstrual_drinks" -> return R.drawable.nourishing_menstrual_drinks

            "vaginal_health" -> return R.drawable.vaginal_health

            "pms_without_period" -> return R.drawable.pms_without_period

            "pre_menstrual_nutrition" -> return R.drawable.pre_menstrual_nutrition

            "boosting_male_fertility_and_sperm_count" -> return R.drawable.boosting_male_fertility_and_sperm_count

            "improving_egg_quality_for_better_fertility" -> return R.drawable.improving_egg_quality_for_better_fertility

            "male_fertility_and_conception" -> return R.drawable.male_fertility_and_conception

            "boost_fertility_naturally" -> return R.drawable.boost_fertility_naturally

            "stop_discharge" -> return R.drawable.stop_discharge

            "pms_normality" -> return R.drawable.pms_normality

            "age_for_sex" -> return R.drawable.age_for_sex

            "pms_hormone" -> return R.drawable.pms_hormone

            "food_for_irregular_periods" -> return R.drawable.food_for_irregular_periods

            "period_supportive_foods" -> return R.drawable.period_supportive_foods

            "balance_hormones_for_pms" -> return R.drawable.balance_hormones_for_pms

            "improve_pms_mood" -> return R.drawable.improve_pms_mood

            "hormones_fertility_and_reproductive_health" -> return R.drawable.hormones_fertility_and_reproductive_health

            "improve_hormonal_health" -> return R.drawable.improve_hormonal_health

            "discharge_time" -> return R.drawable.discharge_time

            "smell_good" -> return R.drawable.smell_good

            "excessive_discharge" -> return R.drawable.excessive_discharge

            "clean_virgin" -> return R.drawable.clean_virgin

            "changing_pms" -> return R.drawable.changing_pms

            "ready_for_sex" -> return R.drawable.ready_for_sex

            "mood_and_hormonal_imbalance" -> return R.drawable.mood_and_hormonal_imbalance

            "smoking_and_menstrual_cycle" -> return R.drawable.smoking_and_menstrual_cycle

            "exercise_and_hormone_regulation" -> return R.drawable.exercise_and_hormone_regulation

            "smoking_and_menstrual_regularity" -> return R.drawable.smoking_and_menstrual_regularity

            "pms_duration" -> return R.drawable.pms_duration

            "abnormal_pms" -> return R.drawable.abnormal_pms

            "natural_pms_relief" -> return R.drawable.natural_pms_relief

            "avoid_pms_foods" -> return R.drawable.avoid_pms_foods

            "stress_and_menstrual_cycle" -> return R.drawable.stress_and_menstrual_cycle

            "period_and_labor_pain_comparison" -> return R.drawable.period_and_labor_pain_comparison

            "cramps_explained" -> return R.drawable.cramps_explained

            "hormonal_imbalances_and_weight_gain_in_girls" -> return R.drawable.hormonal_imbalances_and_weight_gain_in_girls

            "hormonal_health_and_weight_management" -> return R.drawable.hormonal_health_and_weight_management

            "stress_and_hormonal_balance" -> return R.drawable.stress_and_hormonal_balance

            "pain_and_fertility_connection" -> return R.drawable.pain_and_fertility_connection

            "marriage_effect_on_period_pain" -> return R.drawable.marriage_effect_on_period_pain

            "duration_of_menstrual_pain" -> return R.drawable.duration_of_menstrual_pain

            "managing_menstrual_pain_at_work_or_school" -> return R.drawable.managing_menstrual_pain_at_work_or_school

            "lifestyle_and_fertility" -> return R.drawable.lifestyle_and_fertility

            "sleep_and_menstrual_cycle" -> return R.drawable.sleep_and_menstrual_cycle

            "weight_and_menstrual_cycle" -> return R.drawable.weight_and_menstrual_cycle

            "diet_and_menstrual_cycle" -> return R.drawable.diet_and_menstrual_cycle

            "reducing_menstrual_pain" -> return R.drawable.reducing_menstrual_pain

            "painful_periods_and_fertility" -> return R.drawable.painful_periods_and_fertility

            "yogas_potential_impact_on_menstrual_regularity" -> return R.drawable.yogas_potential_impact_on_menstrual_regularity

            "yoga_and_blood_flow_during_menstruation" -> return R.drawable.yoga_and_blood_flow_during_menstruation

            "maintaining_energy_and_activity_during_menstruation" -> return R.drawable.maintaining_energy_and_activity_during_menstruation

            "anxiety_and_menstrual_pain" -> return R.drawable.anxiety_and_menstrual_pain

            "natural_relief_for_menstrual_pain" -> return R.drawable.natural_relief_for_menstrual_pain

            "yoga_and_exercises_impact_on_duration_and_flow_of_periods" -> return R.drawable.yoga_and_exercises_impact_on_duration_and_flow_of_periods

            "yoga_and_exercise_for_enhanced_fertility_during_ovulation" -> return R.drawable.yoga_and_exercise_for_enhanced_fertility_during_ovulation

            "yoga_and_exercise_for_improved_cervical_mucus_quality" -> return R.drawable.yoga_and_exercise_for_improved_cervical_mucus_quality

            "yoga_and_exercise_during_periods" -> return R.drawable.yoga_and_exercise_during_periods

            "reducing_stress_and_promoting_relaxation_with_yoga_during_ovulation" -> return R.drawable.reducing_stress_and_promoting_relaxation_with_yoga_during_ovulation

            "best_sperm_for_pregnancy" -> return R.drawable.best_sperm_for_pregnancy
            "causes_of_irregular_periods" -> return R.drawable.causes_of_irregular_periods
            "alcohol_and_egg_quality" -> return R.drawable.alcohol_and_egg_quality
            "most_fertile_sperm" -> return R.drawable.most_fertile_sperm
            "normal_irregular_periods" -> return R.drawable.normal_irregular_periods
            "alcohol_and_period_flow" -> return R.drawable.alcohol_and_period_flow
            "pregnancy_and_missed_ovulation" -> return R.drawable.pregnancy_and_missed_ovulation
            "preparing_for_pregnancy" -> return R.drawable.preparing_for_pregnancy
            "high_sex_on_ovulation" -> return R.drawable.high_sex_on_ovulation
            "making_period_come_faster" -> return R.drawable.making_period_come_faster
            "watery_sperm_and_pregnancy" -> return R.drawable.watery_sperm_and_pregnancy
            "intercourse_timing_with_ovulation" -> return R.drawable.intercourse_timing_with_ovulation
            "unprotected_sex" -> return R.drawable.unprotected_sex
            "increasing_ovulation_chances" -> return R.drawable.increasing_ovulation_chances
            "fertility_symptoms" -> return R.drawable.fertility_symptoms
            "get_pregnant_faster" -> return R.drawable.get_pregnant_faster
            "folic_acid_and_egg_quality" -> return R.drawable.folic_acid_and_egg_quality
            "watery_sperm_pregnancy" -> return R.drawable.watery_sperm_pregnancy
            "irregular_periods" -> return R.drawable.irregular_periods
            "sperm_to_egg" -> return R.drawable.sperm_to_egg
            "sex_and_immune_system" -> return R.drawable.sex_and_immune_system
            "sperm_staying_inside" -> return R.drawable.sperm_staying_inside
            "hormones_and_ovulation_pain" -> return R.drawable.hormones_and_ovulation_pain
            "best_days_for_pregnancy" -> return R.drawable.exercise_for_menstrual_pain_and_mood_improvement
            "age_and_ovulation" -> return R.drawable.age_and_ovulation
            "male_fertility_by_age" -> return R.drawable.male_fertility_by_age
            "regular_menstrual_cycle" -> return R.drawable.regular_menstrual_cycle
            "alcohol_and_ovulation" -> return R.drawable.alcohol_and_ovulation
            "getting_not_pregnant" -> return R.drawable.getting_not_pregnant
            "hormones_and_ovary_pain" -> return R.drawable.hormones_and_ovary_pain
            "healthy_menstrual_cycles" -> return R.drawable.healthy_menstrual_cycles
            "ovulating_without_period" -> return R.drawable.ovulating_without_period
            "cramps_and_pregnancy" -> return R.drawable.cramps_and_pregnancy
            "signs_of_abnormal_period" -> return R.drawable.signs_of_abnormal_period
            "behavior_on_ovulation" -> return R.drawable.behavior_on_ovulation
            "birth_control_for_periods" -> return R.drawable.birth_control_for_periods
            "causes_of_ovulation_pain" -> return R.drawable.causes_of_ovulation_pain
            "causes_of_failed_conception" -> return R.drawable.causes_of_failed_conception
            "pushing_period_faster" -> return R.drawable.pushing_period_faster
            "sex_while_sick" -> return R.drawable.sex_while_sick
            "best_month_for_pregnancy" -> return R.drawable.best_month_for_pregnancy
            "intercourse_after_ovulation" -> return R.drawable.intercourse_after_ovulation
            "ovulation_pain_and_infertility" -> return R.drawable.ovulation_pain_and_infertility
            "amount_of_sperm_for_pregnancy" -> return R.drawable.amount_of_sperm_for_pregnancy
            "safe_alcohol_for_fertility" -> return R.drawable.safe_alcohol_for_fertility
            "signs_of_irregular_periods" -> return R.drawable.signs_of_irregular_periods
            "factors_for_fertility" -> return R.drawable.factors_for_fertility
            "female_fertility" -> return R.drawable.female_fertility
            "leaving_sperm_overnight" -> return R.drawable.leaving_sperm_overnight
            "egg_released" -> return R.drawable.egg_released
            "increasing_chances_of_conception" -> return R.drawable.increasing_chances_of_conception
            "late_period_remedies" -> return R.drawable.late_period_remedies
            "color_of_ovulation_discharge" -> return R.drawable.color_of_ovulation_discharge
            "signs_of_late_period" -> return R.drawable.signs_of_late_period
            "solving_irregular_periods" -> return R.drawable.solving_irregular_periods
            else -> {
                Log.e("MYTAG", "ErrorNo: setImage=:" + s)
                return R.drawable.exercise_for_menstrual_pain_and_mood_improvement
            }
        }
    }
}
