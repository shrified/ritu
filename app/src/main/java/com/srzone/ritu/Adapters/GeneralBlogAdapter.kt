package com.srzone.ritu.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.Activities.ReadBlogActivity
import com.srzone.ritu.Model.Blog
import com.srzone.ritu.Model.FeaturedBlog
import com.srzone.ritu.R
import com.srzone.ritu.ThemesFiles.MyThemeHandler
import com.srzone.ritu.Utils.Utils

class GeneralBlogAdapter(
    var list: MutableList<Blog?>,
    var featuredBlogList: MutableList<FeaturedBlog?>,
    private val orList: MutableList<String?>?,
    var activity: Activity,
    private val mix: Boolean
) : RecyclerView.Adapter<GeneralBlogAdapter.ViewHolder>() {
    private var lastPosition = -1

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return if (i == VIEW_TYPE_HORIZONTAL) {
            HorizontalViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.features_blog_item_with_recycler, viewGroup, false)
            )
        } else {
            VerticalViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.blogs_item_layout, viewGroup, false)
            )
        }
    }

    override fun getItemViewType(i: Int): Int {
        return if (this.mix) {
            if (i == 50 || i == 0 || i == this.list.size - 1) VIEW_TYPE_HORIZONTAL else VIEW_TYPE_VERTICAL
        } else {
            VIEW_TYPE_VERTICAL
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") i: Int) {
        val blog = list[i] ?: return
        
        if (viewHolder is VerticalViewHolder) {
            viewHolder.blogCoverImg.clipToOutline = true
            viewHolder.blogCoverImg.setImageResource(Utils.setImage(blog.imgPath ?: ""))

            viewHolder.blogTitleTv.text = blog.heading
            var trim = (blog.body ?: "").trim()
            try {
                if (trim.startsWith("<h1>") && "</h1>" in trim) {
                    val parts = trim.split("</h1>".toRegex(), 2)
                    if (parts.size > 1) {
                        trim = parts[1].trim()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            viewHolder.blogDescTv.text = Utils.htmlToText(trim)
            viewHolder.itemView.setOnClickListener {
                openBlogActivity(i)
            }
            setAnimation(viewHolder.itemView, i)
        } else if (viewHolder is HorizontalViewHolder) {
            featuredBlogList.shuffle()
            
            if (MyThemeHandler().getAppTheme(this.activity)?.isDark == true) {
                viewHolder.forYouTv.setTextColor(ContextCompat.getColor(activity, R.color.white))
            }
            
            viewHolder.recyclerView.layoutManager = LinearLayoutManager(
                this.activity,
                RecyclerView.HORIZONTAL,
                false
            )
            
            val nonNullFeaturedList = featuredBlogList.filterNotNull().toMutableList()
            viewHolder.recyclerView.adapter = FeaturedBlogAdapter(
                nonNullFeaturedList,
                this.activity
            )

            viewHolder.blogCoverImg.setImageResource(Utils.setImage(blog.imgPath ?: ""))
            viewHolder.blogTitleTv.text = blog.heading
            
            var body = blog.body
            viewHolder.bottomBlogArea.setOnClickListener {
                openBlogActivity(i)
            }
            
            if (body != null) {
                if (body.startsWith("<h1>")) {
                    try {
                        val parts = body.split("</h1>".toRegex(), 2)
                        if (parts.size > 1) {
                            body = parts[1].trim()
                        }
                    } catch (e2: Exception) {
                        e2.printStackTrace()
                    }
                }
                viewHolder.blogDescTv.text = Utils.htmlToText(body)
            }
            
            if (i == this.list.size - 1) {
                reverseLinearLayout(viewHolder.itemView as LinearLayout)
                viewHolder.forYouTv.visibility = View.GONE
            }
        }
    }

    private fun openBlogActivity(i: Int) {
        val blog = list[i] ?: return
        val intent = Intent(this.activity, ReadBlogActivity::class.java)
        intent.putExtra("heading", blog.heading)
        intent.putExtra("imgRes", blog.imgPath)
        intent.putExtra("body", blog.body)
        intent.putExtra("color", blog.color)
        intent.putExtra("dark", blog.isDark)
        this.activity.startActivity(intent)
    }

    private fun setAnimation(view: View, i: Int) {
        if (i > this.lastPosition) {
            view.startAnimation(
                AnimationUtils.loadAnimation(
                    this.activity,
                    R.anim.top_slider_animation
                )
            )
            this.lastPosition = i
        }
    }

    override fun getItemCount(): Int {
        return this.list.size
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class HorizontalViewHolder(view: View) : ViewHolder(view) {
        val blogCoverImg: ImageView = view.findViewById(R.id.blogCoverImg)
        val blogDescTv: TextView = view.findViewById(R.id.blogDescTv)
        val blogTitleTv: TextView = view.findViewById(R.id.blogTitleTv)
        val bottomBlogArea: ConstraintLayout = view.findViewById(R.id.bottomBlogArea)
        val forYouTv: TextView = view.findViewById(R.id.forYouTv)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
    }

    class VerticalViewHolder(view: View) : ViewHolder(view) {
        val blogCoverImg: ImageView = view.findViewById(R.id.blogCoverImg)
        val blogDescTv: TextView = view.findViewById(R.id.blogDescTv)
        val blogTitleTv: TextView = view.findViewById(R.id.blogTitleTv)
    }

    private fun reverseLinearLayout(linearLayout: LinearLayout) {
        val views = mutableListOf<View>()
        for (i in 0 until linearLayout.childCount) {
            views.add(linearLayout.getChildAt(i))
        }
        linearLayout.removeAllViews()
        for (i in views.indices.reversed()) {
            linearLayout.addView(views[i])
        }
    }

    companion object {
        const val TAG: String = "BlogsTAG"
        private const val VIEW_TYPE_HORIZONTAL = 1
        private const val VIEW_TYPE_VERTICAL = 0
    }
}
