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
import com.srzone.ritu.Adapters.FeaturedBlogAdapter
import com.srzone.ritu.Adapters.GeneralBlogAdapter
import com.srzone.ritu.Databases.LikesHandler
import com.srzone.ritu.Databases.RecentsHandler
import com.srzone.ritu.Model.Blog
import com.srzone.ritu.Model.FeaturedBlog
import com.srzone.ritu.R
import com.srzone.ritu.ThemesFiles.MyThemeHandler
import com.srzone.ritu.Utils.ImageUtils
import com.srzone.ritu.Utils.Utils
import com.srzone.ritu.databinding.FragmentBlogsBinding
import java.util.Locale

class BlogsFragment : Fragment() {
    private var binding: FragmentBlogsBinding? = null
    private var themeColor = 0

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        binding = FragmentBlogsBinding.inflate(layoutInflater, viewGroup, false)
        showDiscoverData()
        setUpTheme()

        binding?.apply {
            discoverBtn.setOnClickListener {
                onDiscoverBtnClicked()
            }
            savedBtn.setOnClickListener {
                onSavedBtnClicked()
            }
            recentBtn.setOnClickListener {
                onRecentBtnClicked()
            }
        }
        return binding?.root
    }

    private fun onDiscoverBtnClicked() {
        binding?.let {
            activateBtn(it.discoverBtn)
            deActivateBtn(it.savedBtn)
            deActivateBtn(it.recentBtn)
        }
        showDiscoverData()
    }

    private fun onSavedBtnClicked() {
        binding?.let {
            deActivateBtn(it.discoverBtn)
            activateBtn(it.savedBtn)
            deActivateBtn(it.recentBtn)
        }
        showSavedData()
    }

    private fun onRecentBtnClicked() {
        binding?.let {
            deActivateBtn(it.discoverBtn)
            deActivateBtn(it.savedBtn)
            activateBtn(it.recentBtn)
        }
        showRecentData()
    }

    private fun activateBtn(linearLayout: LinearLayout) {
        val icon = linearLayout.getChildAt(0) as? ImageView
        val text = linearLayout.getChildAt(1) as? TextView
        
        icon?.let { ImageUtils.setTint(it, R.color.white) }
        text?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        Utils.setTint(linearLayout, themeColor)
    }

    private fun deActivateBtn(linearLayout: LinearLayout) {
        val icon = linearLayout.getChildAt(0) as? ImageView
        val text = linearLayout.getChildAt(1) as? TextView
        
        icon?.let { ImageUtils.setTint(it, themeColor) }
        text?.setTextColor(ContextCompat.getColor(requireContext(), themeColor))
        Utils.setTint(linearLayout, R.color.white)
    }

    private fun setUpTheme() {
        val theme = MyThemeHandler().getAppTheme(requireActivity())
        themeColor = theme?.themeColor ?: R.color.theme1
        
        binding?.let {
            activateBtn(it.discoverBtn)
            deActivateBtn(it.savedBtn)
            deActivateBtn(it.recentBtn)
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

    private fun showRecentData() {
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
                    try {
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
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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
        
        val allRecents = RecentsHandler(activity).allRecents
        val recentBlogs = mutableListOf<Blog?>()
        val recentFeaturedBlogs = mutableListOf<FeaturedBlog?>()
        
        for (recents in allRecents) {
            if (recents?.title != null) {
                for (featuredBlog in featuredBlogList) {
                    if (featuredBlog.detail == recents.title) {
                        recentFeaturedBlogs.add(featuredBlog)
                    }
                }
            } else if (recents?.heading != null) {
                for (blog in blogList) {
                    if (blog.heading == recents.heading) {
                        recentBlogs.add(blog)
                    }
                }
            }
        }
        
        recentBlogs.reverse()
        recentFeaturedBlogs.reverse()
        
        binding?.apply {
            verticalRecyclerView.layoutManager = LinearLayoutManager(activity)
            horizontalRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            
            verticalRecyclerView.adapter = GeneralBlogAdapter(
                recentBlogs,
                recentFeaturedBlogs,
                titleList,
                requireActivity(),
                false
            )
            horizontalRecyclerView.adapter = FeaturedBlogAdapter(
                recentFeaturedBlogs.filterNotNull().toMutableList(),
                requireActivity()
            )
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
}
