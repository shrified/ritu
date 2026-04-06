package com.srzone.ritu.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.gson.internal.LinkedTreeMap
import com.srzone.ritu.Adapters.CategoryBlogsAdapter
import com.srzone.ritu.Adapters.FeaturedBlogAdapter
import com.srzone.ritu.Adapters.GeneralBlogAdapter
import com.srzone.ritu.Databases.LikesHandler
import com.srzone.ritu.Model.Blog
import com.srzone.ritu.Model.BlogCategory
import com.srzone.ritu.Model.CategoryFeaturedBlog
import com.srzone.ritu.Model.FeaturedBlog
import com.srzone.ritu.R
import com.srzone.ritu.ThemesFiles.MyThemeHandler
import com.srzone.ritu.Utils.ImageUtils
import com.srzone.ritu.Utils.Utils
import com.srzone.ritu.databinding.FragmentBlogsBinding
import java.util.Locale
import kotlin.collections.get

class BlogsFragment : Fragment() {
    private var binding: FragmentBlogsBinding? = null
    private var themeColorRes = R.color.app_primary_color

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        binding = FragmentBlogsBinding.inflate(layoutInflater, viewGroup, false)
        setUpTheme()
        showDiscoverData()

        binding?.apply {
            discoverBtn.setOnClickListener {
                onDiscoverBtnClicked()
            }
            categoryBtn.setOnClickListener {
                onCategoryBtnClicked()
            }
            savedBtn.setOnClickListener {
                onSavedBtnClicked()
            }
        }
        return binding?.root
    }

    private fun onDiscoverBtnClicked() {
        binding?.let {
            activateBtn(it.discoverBtn)
            deActivateBtn(it.savedBtn)
            deActivateBtn(it.categoryBtn)
        }
        showDiscoverData()
    }

    private fun onCategoryBtnClicked() {
        binding?.let {
            deActivateBtn(it.discoverBtn)
            deActivateBtn(it.savedBtn)
            activateBtn(it.categoryBtn)
            it.discoverRecycler.visibility = View.GONE
            it.othersContentArea.visibility = View.VISIBLE
        }
        showCategoryData()
    }

    private fun onSavedBtnClicked() {
        binding?.let {
            deActivateBtn(it.discoverBtn)
            activateBtn(it.savedBtn)
            deActivateBtn(it.categoryBtn)
        }
        showSavedData()
    }

    private fun activateBtn(cardView: MaterialCardView) {
        val linearLayout = cardView.getChildAt(0) as? LinearLayout ?: return
        val icon = linearLayout.getChildAt(0) as? ImageView
        val text = linearLayout.getChildAt(1) as? TextView

        cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), themeColorRes))
        icon?.let { ImageUtils.setTint(it, R.color.white) }
        text?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun deActivateBtn(cardView: MaterialCardView) {
        val linearLayout = cardView.getChildAt(0) as? LinearLayout ?: return
        val icon = linearLayout.getChildAt(0) as? ImageView
        val text = linearLayout.getChildAt(1) as? TextView

        cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        icon?.let { ImageUtils.setTint(it, themeColorRes) } // ✅ Pass Res ID, not color value
        text?.setTextColor(ContextCompat.getColor(requireContext(), themeColorRes))
    }

    private fun setUpTheme() {
        val theme = MyThemeHandler().getAppTheme(requireActivity())
        themeColorRes = theme?.themeColor ?: R.color.app_primary_color

        binding?.let {
            activateBtn(it.discoverBtn)
            deActivateBtn(it.savedBtn)
        }
    }

    private fun showDiscoverData() {
        binding?.let {
            it.discoverRecycler.visibility = View.VISIBLE
            it.othersContentArea.visibility = View.GONE
        }

        val blogList = mutableListOf<Blog?>()
        val featuredBlogList = mutableListOf<FeaturedBlog?>()
        val titleList = mutableListOf<String?>()

        val context = requireContext()
        val lang = Locale.getDefault().language
        val readAssetFile = Utils.readAssetFile(context, "$lang.json")
        val readAssetFile2 = Utils.readAssetFile(context, "en.json")

        if (readAssetFile != null && readAssetFile2 != null) {
            val minSize = minOf(readAssetFile.size, readAssetFile2.size)
            for (i in 0 until minSize) {
                val hashMap = readAssetFile[i]
                val enMap = readAssetFile2[i]

                if (enMap != null && hashMap != null) {
                    titleList.add(enMap["title"]?.toString())
                    featuredBlogList.add(
                        FeaturedBlog(
                            hashMap["heading"]?.toString(),
                            hashMap["body"]?.toString(),
                            Utils.lowerUnder(enMap["title"]?.toString() ?: ""),
                            hashMap["title"]?.toString(),
                            enMap["color"]?.toString(),
                            enMap["dark"] as? Boolean ?: false
                        )
                    )
                }
            }
        }

        val readAssetFile3 = Utils.readAssetFile(context, "${lang}_g.json")
        val readAssetFile4 = Utils.readAssetFile(context, "en_g.json")

        if (readAssetFile3 != null && readAssetFile4 != null) {
            val minSize = minOf(readAssetFile3.size, readAssetFile4.size)
            for (i in 0 until minSize) {
                val hashMap2 = readAssetFile3[i]
                val enMap2 = readAssetFile4[i]

                if (hashMap2 != null && enMap2 != null) {
                    blogList.add(
                        Blog(
                            hashMap2["heading"]?.toString(),
                            hashMap2["body"]?.toString(),
                            Utils.lowerUnder(enMap2["heading"]?.toString() ?: ""),
                            enMap2["color"]?.toString(),
                            enMap2["dark"] as? Boolean ?: false
                        )
                    )
                }
            }
        }

        blogList.shuffle()

        binding?.discoverRecycler?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = GeneralBlogAdapter(
                blogList,
                featuredBlogList,
                titleList,
                requireActivity(),
                true
            )
        }
    }

    private fun showCategoryData() {
        val context = context ?: return
        binding?.verticalRecyclerView?.visibility = View.VISIBLE

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
        binding?.verticalRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CategoryBlogsAdapter(blogCategories, activity)
        }
    }

    private fun showSavedData() {
        binding?.let {
            it.discoverRecycler.visibility = View.GONE
            it.othersContentArea.visibility = View.VISIBLE
        }

        val blogList = mutableListOf<Blog>()
        val featuredBlogList = mutableListOf<FeaturedBlog>()
        val titleList = mutableListOf<String?>()

        val context = requireContext()
        val lang = Locale.getDefault().language
        val readAssetFile = Utils.readAssetFile(context, "$lang.json")
        val readAssetFile2 = Utils.readAssetFile(context, "en.json")

        if (readAssetFile != null && readAssetFile2 != null) {
            val minSize = minOf(readAssetFile.size, readAssetFile2.size)
            for (i in 0 until minSize) {
                val hashMap = readAssetFile[i]
                val enMap = readAssetFile2[i]

                if (enMap != null && hashMap != null) {
                    titleList.add(enMap["title"]?.toString())
                    featuredBlogList.add(
                        FeaturedBlog(
                            hashMap["heading"]?.toString(),
                            hashMap["body"]?.toString(),
                            Utils.lowerUnder(enMap["title"]?.toString() ?: ""),
                            hashMap["title"]?.toString(),
                            enMap["color"]?.toString(),
                            enMap["dark"] as? Boolean ?: false
                        )
                    )
                }
            }
        }

        val readAssetFile3 = Utils.readAssetFile(context, "${lang}_g.json")
        val readAssetFile4 = Utils.readAssetFile(context, "en_g.json")

        if (readAssetFile3 != null && readAssetFile4 != null) {
            val minSize = minOf(readAssetFile3.size, readAssetFile4.size)
            for (i in 0 until minSize) {
                val hashMap2 = readAssetFile3[i]
                val enMap2 = readAssetFile4[i]

                if (hashMap2 != null && enMap2 != null) {
                    blogList.add(
                        Blog(
                            hashMap2["heading"]?.toString(),
                            hashMap2["body"]?.toString(),
                            Utils.lowerUnder(enMap2["heading"]?.toString() ?: ""),
                            enMap2["color"]?.toString(),
                            enMap2["dark"] as? Boolean ?: false
                        )
                    )
                }
            }
        }

        val allLikes = LikesHandler(activity).allLikes
        val savedBlogs = mutableListOf<Blog?>()
        val savedFeaturedBlogs = mutableListOf<FeaturedBlog?>()

        for (likes in allLikes) {
            if (likes?.title != null) {
                for (featuredBlog in featuredBlogList) {
                    if (featuredBlog.detail == likes.title) {
                        savedFeaturedBlogs.add(featuredBlog)
                    }
                }
            } else if (likes?.heading != null) {
                for (blog in blogList) {
                    if (blog.heading == likes.heading) {
                        savedBlogs.add(blog)
                    }
                }
            }
        }

        savedBlogs.reverse()
        savedFeaturedBlogs.reverse()

        binding?.apply {
            verticalRecyclerView.layoutManager = LinearLayoutManager(activity)
            horizontalRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            verticalRecyclerView.adapter = GeneralBlogAdapter(
                savedBlogs,
                savedFeaturedBlogs,
                titleList,
                requireActivity(),
                false
            )
            horizontalRecyclerView.adapter = FeaturedBlogAdapter(
                savedFeaturedBlogs.filterNotNull().toMutableList(),
                requireActivity()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    companion object {
         val categoriesRes = intArrayOf(
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
         val categories = arrayOf(
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
