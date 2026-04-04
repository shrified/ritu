package com.srzone.ritu.Activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.Adapters.FeaturedBlogAdapter
import com.srzone.ritu.Adapters.InnerArticlesAdapter
import com.srzone.ritu.Databases.Entities.Likes
import com.srzone.ritu.Databases.Entities.Recents
import com.srzone.ritu.Databases.LikesHandler
import com.srzone.ritu.Databases.Params
import com.srzone.ritu.Databases.RecentsHandler
import com.srzone.ritu.Model.Blog
import com.srzone.ritu.Model.FeaturedBlog
import com.srzone.ritu.R
import com.srzone.ritu.Utils.ImageUtils
import com.srzone.ritu.Utils.Utils
import com.srzone.ritu.databinding.ActivityReadBlogBinding
import java.util.Locale

class ReadBlogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadBlogBinding
    private var heading: String? = null
    private var isCategory = false
    private lateinit var likesHandler: LikesHandler
    private lateinit var recentsHandler: RecentsHandler
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.makeTransparentStatusBar(this)

        likesHandler = LikesHandler(this)
        recentsHandler = RecentsHandler(this)

        heading = intent.getStringExtra("heading")
        title = intent.getStringExtra("title")
        isCategory = intent.getBooleanExtra("categories", false)

        if (intent.getBooleanExtra("dark", false)) {
            ImageUtils.setTint(binding.likeButton, R.color.white)
            ImageUtils.setTint(binding.backButton, R.color.white)
        }

        binding.backButton.setOnClickListener { finish() }
        binding.blogHeadingTv.text = heading ?: ""

        val body = intent.getStringExtra("body") ?: ""
        binding.blogBodyTv.text = HtmlCompat.fromHtml(body, HtmlCompat.FROM_HTML_MODE_LEGACY)

        val imgRes = intent.getStringExtra("imgRes") ?: ""
        binding.blogImg.setImageResource(Utils.setImage(imgRes))

        checkLike()
        handleRecent()

        if (!isCategory) {
            binding.likeButton.setOnClickListener { handleLike() }
        } else {
            binding.likeButton.visibility = View.GONE
        }

        if (title != null && !isCategory) {
            loadHorizontalArticles()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                loadGeneralArticles()
            }, 300L)
        }
    }

    private fun loadHorizontalArticles() {
        val arrayList = ArrayList<FeaturedBlog>()
        val localFile = Utils.readAssetFile(this, Locale.getDefault().language + ".json")
        val enFile = Utils.readAssetFile(this, "en.json")

        if (localFile != null && enFile != null) {
            for (i in localFile.indices) {
                val item = localFile[i] ?: continue
                val enItem = enFile.getOrNull(i) ?: continue
                if (title != null && title != item["title"]?.toString()) {
                    arrayList.add(
                        FeaturedBlog(
                            item["heading"]?.toString() ?: "",
                            item["body"]?.toString() ?: "",
                            Utils.lowerUnder(enItem["title"]?.toString() ?: ""),
                            item["title"]?.toString() ?: "",
                            enItem["color"]?.toString() ?: "#ffffff",
                            enItem["dark"] as? Boolean ?: false
                        )
                    )
                }
            }
        }

        arrayList.shuffle()
        val subList = arrayList.subList(0, minOf(7, arrayList.size)).toMutableList()
        binding.featuredBlogsRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.featuredBlogsRecycler.adapter = FeaturedBlogAdapter(subList, this)
        showData()
    }

    private fun loadGeneralArticles() {
        val arrayList = ArrayList<Blog?>()
        val localFile = Utils.readAssetFile(this, Locale.getDefault().language + "_g.json")
        val enFile = Utils.readAssetFile(this, "en_g.json")

        if (localFile != null && enFile != null) {
            for (i in localFile.indices) {
                val item = localFile[i] ?: continue
                val enItem = enFile.getOrNull(i) ?: continue
                if (heading != item["heading"]?.toString()) {
                    arrayList.add(
                        Blog(
                            item["heading"]?.toString() ?: "",
                            item["body"]?.toString() ?: "",
                            Utils.lowerUnder(enItem["heading"]?.toString() ?: ""),
                            enItem["color"]?.toString() ?: "#ffffff",
                            enItem["dark"] as? Boolean ?: false
                        )
                    )
                }
            }
        }

        arrayList.shuffle()
        val subList = arrayList.subList(0, minOf(5, arrayList.size)).toMutableList()
        binding.generalBlogsRecycler.layoutManager = LinearLayoutManager(this)
        binding.generalBlogsRecycler.adapter = InnerArticlesAdapter(subList, this)
        showData()
    }

    private fun showData() {
        binding.blogImg.visibility = View.VISIBLE
        binding.contentArea.visibility = View.VISIBLE
        binding.pb.visibility = View.GONE
    }

    private fun handleLike() {
        val currentTitle = title
        if (currentTitle != null) {
            val like = likesHandler.getLikeByParam(currentTitle, Params.KEY_LIKES_TITLE)
            if (like == null) {
                likesHandler.addLike(Likes().apply { this.title = currentTitle })
                setLiked(true)
            } else {
                likesHandler.deleteLike(like.id.toString())
                setLiked(false)
            }
        } else {
            val like = likesHandler.getLikeByParam(heading ?: "", Params.KEY_LIKES_HEADING)
            if (like == null) {
                likesHandler.addLike(Likes().apply { this.heading = this@ReadBlogActivity.heading })
                setLiked(true)
            } else {
                likesHandler.deleteLike(like.id.toString())
                setLiked(false)
            }
        }
    }

    private fun setLiked(liked: Boolean) {
        val res = if (liked) R.drawable.ic_liked else R.drawable.ic_like
        binding.likeButton.setImageResource(res)
    }

    private fun checkLike() {
        val currentTitle = title
        val isLiked = if (currentTitle != null) {
            likesHandler.getLikeByParam(currentTitle, Params.KEY_LIKES_TITLE) != null
        } else {
            likesHandler.getLikeByParam(heading ?: "", Params.KEY_LIKES_HEADING) != null
        }
        setLiked(isLiked)
    }

    private fun handleRecent() {
        val currentTitle = title
        if (currentTitle != null) {
            val recent = recentsHandler.getRecentByParam(currentTitle, Params.KEY_RECENTS_TITLE)
            if (recent != null) recentsHandler.deleteRecent(recent.id.toString())
            recentsHandler.addRecent(Recents().apply { this.title = currentTitle })
        } else {
            val recent = recentsHandler.getRecentByParam(heading ?: "", Params.KEY_RECENTS_HEADING)
            if (recent != null) recentsHandler.deleteRecent(recent.id.toString())
            recentsHandler.addRecent(Recents().apply { this.heading = this@ReadBlogActivity.heading })
        }
    }
}
