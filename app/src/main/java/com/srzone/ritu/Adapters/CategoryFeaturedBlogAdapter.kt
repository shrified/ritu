package com.srzone.ritu.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.Activities.GenericReadingActivity
import com.srzone.ritu.Model.CategoryFeaturedBlog
import com.srzone.ritu.R
import com.srzone.ritu.Utils.Utils

class CategoryFeaturedBlogAdapter(
    var blogList: MutableList<CategoryFeaturedBlog>,
    var activity: Activity
) : RecyclerView.Adapter<CategoryFeaturedBlogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.category_featured_blog_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val categoryFeaturedBlog = this.blogList[position]
        viewHolder.headingTv.text = categoryFeaturedBlog.detail
        viewHolder.imageView.setImageResource(Utils.setImage(categoryFeaturedBlog.imgPath ?: ""))

        if (categoryFeaturedBlog.isDark) {
            viewHolder.headingTv.setTextColor(ContextCompat.getColor(this.activity, R.color.white))
        }

        viewHolder.itemView.setOnClickListener {
            openBlogActivity(position)
        }
    }

    private fun openBlogActivity(position: Int) {
        val blog = this.blogList[position]
        val intent = Intent(this.activity, GenericReadingActivity::class.java)
        intent.putExtra("heading", blog.heading)
        intent.putExtra("imgRes", blog.imgPath)
        intent.putExtra("body", blog.body)
        intent.putExtra("title", blog.detail)
        intent.putExtra("categories", true)
        intent.putExtra("color", "#FFFFFF")
        intent.putExtra("dark", blog.isDark)
        this.activity.startActivity(intent)
        
        if (this.activity is GenericReadingActivity) {
            this.activity.finish()
        }
    }

    override fun getItemCount(): Int {
        return this.blogList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView? = view.findViewById(R.id.cardView)
        val headingTv: TextView = view.findViewById(R.id.headingTv)
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }
}
