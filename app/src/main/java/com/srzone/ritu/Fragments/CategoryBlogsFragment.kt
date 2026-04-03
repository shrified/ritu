package com.srzone.ritu.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.srzone.ritu.Adapters.CategoryBlogsAdapter
import com.srzone.ritu.Model.BlogCategory
import com.srzone.ritu.Model.CategoryFeaturedBlog
import com.srzone.ritu.R
import com.srzone.ritu.Utils.Utils
import com.srzone.ritu.databinding.FragmentCategoryBlogsBinding
import com.google.gson.internal.LinkedTreeMap
import java.util.Locale

class CategoryBlogsFragment : Fragment() {
    private var binding: FragmentCategoryBlogsBinding? = null
    
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        binding = FragmentCategoryBlogsBinding.inflate(layoutInflater, viewGroup, false)
        showCombinedData()
        return binding!!.root
    }

    private fun showCombinedData() {
        val context = context ?: return
        binding?.articlesRecycler?.visibility = View.VISIBLE
        
        val blogCategories = mutableListOf<BlogCategory?>()
        val lang = Locale.getDefault().language
        val readAssetFile = Utils.readAssetFile(context, "${lang}_c.json")
        val readAssetFile2 = Utils.readAssetFile(context, "en_c.json")
        
        if (readAssetFile != null && readAssetFile2 != null) {
            val minSize = minOf(readAssetFile.size, readAssetFile2.size)
            for (i in 0 until minSize) {
                val hashMap = readAssetFile[i]
                val enMap = readAssetFile2[i]
                
                if (hashMap != null && enMap != null) {
                    val data = hashMap["data"] as? ArrayList<*>
                    val enData = enMap["data"] as? ArrayList<*>
                    val blogList = mutableListOf<CategoryFeaturedBlog?>()
                    
                    if (data != null && enData != null) {
                        val minDataSize = minOf(data.size, enData.size)
                        for (i2 in 0 until minDataSize) {
                            val linkedTreeMap = data[i2] as? LinkedTreeMap<*, *>
                            val enLinkedTreeMap = enData[i2] as? LinkedTreeMap<*, *>
                            
                            if (linkedTreeMap != null && enLinkedTreeMap != null) {
                                val heading = Utils.getStringFromObj(linkedTreeMap["heading"])
                                if (!heading.isNullOrEmpty()) {
                                    blogList.add(
                                        CategoryFeaturedBlog(
                                            heading,
                                            Utils.getStringFromObj(linkedTreeMap["body"]),
                                            Utils.lowerUnder(Utils.getStringFromObj(enLinkedTreeMap["title"]) ?: ""),
                                            Utils.getStringFromObj(linkedTreeMap["title"]),
                                            Utils.getStringFromObj(enLinkedTreeMap["color"]),
                                            Utils.getBoolFromObj(enLinkedTreeMap["dark"])
                                        )
                                    )
                                }
                            }
                        }
                    }
                    
                    blogCategories.add(
                        BlogCategory(
                            hashMap["category"].toString(),
                            categoriesRes[i],
                            blogList
                        )
                    )
                }
            }
        }
        
        blogCategories.shuffle()
        binding?.articlesRecycler?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CategoryBlogsAdapter(blogCategories, activity)
        }
    }

    private fun showSeparateData() {
        val context = context ?: return
        binding?.articlesRecycler?.visibility = View.VISIBLE
        
        val blogCategories = mutableListOf<BlogCategory?>()
        val lang = Locale.getDefault().language
        
        for (i in categories.indices) {
            val categoryStr = categories[i]
            val blogList = mutableListOf<CategoryFeaturedBlog?>()
            
            val readAssetFile = Utils.readAssetFile(context, "${lang}_${categoryStr}.json")
            val readAssetFile2 = Utils.readAssetFile(context, "en_${categoryStr}.json")
            
            if (readAssetFile != null && readAssetFile2 != null) {
                val minSize = minOf(readAssetFile.size, readAssetFile2.size)
                for (i2 in 0 until minSize) {
                    val hashMap = readAssetFile[i2]
                    val enMap = readAssetFile2[i2]
                    
                    if (hashMap != null && enMap != null) {
                        blogList.add(
                            CategoryFeaturedBlog(
                                Utils.getStringFromObj(hashMap["heading"]),
                                Utils.getStringFromObj(hashMap["body"]),
                                Utils.lowerUnder(Utils.getStringFromObj(enMap["title"]) ?: ""),
                                Utils.getStringFromObj(hashMap["title"]),
                                Utils.getStringFromObj(enMap["color"]),
                                Utils.getBoolFromObj(enMap["dark"])
                            )
                        )
                    }
                }
            }
            blogCategories.add(BlogCategory(categoryStr, categoriesRes[i], blogList))
        }
        
        blogCategories.shuffle()
        binding?.articlesRecycler?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CategoryBlogsAdapter(blogCategories, activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private val categoriesRes = intArrayOf(
            R.string.anxiety_and_depression,
            R.string.pain_managment,
            R.string.boost_intimacy,
            R.string.birth_control,
            R.string.sex_worries,
            R.string.mental_stress,
            R.string.healthy_eating,
            R.string.yoga_and_exercise,
            R.string.increase_fertility,
            R.string.sexual_health,
            R.string.hormonal_health,
            R.string.menstrual_pain,
            R.string.high_bleeding_and_hormonal_imbalance,
            R.string.high_bleeding,
            R.string.stress_and_menstrual_cycle,
            R.string.pain_in_fertility_test,
            R.string.juice_during_period,
            R.string.menstrual_cramps,
            R.string.normal_discharge_time,
            R.string.female_reproductive,
            R.string.pms_symptoms
        )
        private val categories = arrayOf(
            "Anxiety and depression",
            "Pain Management",
            "Boost intimacy",
            "Birth control",
            "Sex Worries",
            "Mental Stress",
            "Healthy Eating",
            "Yoga & exercise",
            "Increase Fertility",
            "Sexual Health ",
            "Hormonal Health",
            "Menstrual Pain",
            "High Bleeding and Hormonal Imbalance",
            "High Bleeding",
            "Stress and Menstrual Cycle",
            "Pain in Fertility Test",
            "Juice During Periods",
            "Menstrual Cramps",
            "Normal Discharge Time",
            "Female Reproductive",
            "PMS symptoms"
        )
    }
}
