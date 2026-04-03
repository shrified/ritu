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
import com.srzone.ritu.Activities.ReadBlogActivity
import com.srzone.ritu.Model.FeaturedBlog
import com.srzone.ritu.R
import com.srzone.ritu.Utils.Utils

class FeaturedBlogAdapter(var blogList: MutableList<FeaturedBlog>, var activity: Activity) :
    RecyclerView.Adapter<FeaturedBlogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.featured_blog_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val featuredBlog = this.blogList[position]
        viewHolder.headingTv.text = featuredBlog.detail
        viewHolder.questionTv.text = featuredBlog.heading
        viewHolder.imageView.setImageResource(Utils.setImage(featuredBlog.imgPath ?: ""))

        if (!featuredBlog.isDark) {
            viewHolder.headingTv.setTextColor(ContextCompat.getColor(this.activity, R.color.black))
            viewHolder.opacityView.visibility = View.GONE
        } else {
            viewHolder.headingTv.setTextColor(ContextCompat.getColor(this.activity, R.color.white))
            viewHolder.opacityView.visibility = View.VISIBLE
        }
        
        viewHolder.itemView.setOnClickListener {
            openBlogActivity(position)
        }
    }

    private fun openBlogActivity(position: Int) {
        val blog = this.blogList[position]
        val intent = Intent(this.activity, ReadBlogActivity::class.java)
        intent.putExtra("heading", blog.heading)
        intent.putExtra("imgRes", blog.imgPath)
        intent.putExtra("body", blog.body)
        intent.putExtra("title", blog.detail)
        intent.putExtra("color", blog.color)
        intent.putExtra("dark", blog.isDark)
        this.activity.startActivity(intent)
        
        if (this.activity is ReadBlogActivity) {
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
        val opacityView: View = view.findViewById(R.id.opacityView)
        val questionTv: TextView = view.findViewById(R.id.questionTv)
    }
}
