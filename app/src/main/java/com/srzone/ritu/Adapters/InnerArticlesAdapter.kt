package com.srzone.ritu.Adapters

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.Activities.GenericReadingActivity
import com.srzone.ritu.Model.Blog
import com.srzone.ritu.R
import com.srzone.ritu.Utils.Utils

class InnerArticlesAdapter(var blogList: MutableList<Blog?>, var activity: Activity) :
    RecyclerView.Adapter<InnerArticlesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.inner_articles_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val blog = this.blogList[position] ?: return
        
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val resId = Utils.getResId(blog.imgPath ?: "", activity)
        BitmapFactory.decodeResource(this.activity.resources, resId, options)
        
        // Calculate sample size
        val reqWidth = 200
        val reqHeight = 200
        var inSampleSize = 1
        if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
            val halfHeight = options.outHeight / 2
            val halfWidth = options.outWidth / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        
        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        
        viewHolder.imageView.setImageBitmap(
            BitmapFactory.decodeResource(
                this.activity.resources,
                resId,
                options
            )
        )
        
        viewHolder.headingTv.text = blog.heading
        
        var body = blog.body
        if (body != null) {
            try {
                if (body.startsWith("<h1>") && body.contains("</h1>")) {
                    val parts = body.split("</h1>".toRegex(), 2)
                    if (parts.size > 1) {
                        body = parts[1]
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            viewHolder.descTv.text = Utils.htmlToText(body)
        }
        
        viewHolder.itemView.setOnClickListener {
            openBlogActivity(position)
        }
    }

    private fun openBlogActivity(position: Int) {
        val blog = this.blogList[position] ?: return
        val intent = Intent(this.activity, GenericReadingActivity::class.java)
        intent.putExtra("heading", blog.heading)
        intent.putExtra("imgRes", blog.imgPath)
        intent.putExtra("body", blog.body)
        intent.putExtra("color", blog.color)
        intent.putExtra("dark", blog.isDark)
        this.activity.startActivity(intent)
        this.activity.finish()
    }

    override fun getItemCount(): Int {
        return this.blogList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descTv: TextView = view.findViewById(R.id.descTv)
        val headingTv: TextView = view.findViewById(R.id.headingTv)
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }
}
