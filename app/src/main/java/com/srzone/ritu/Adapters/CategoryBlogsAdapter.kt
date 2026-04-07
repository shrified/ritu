package com.srzone.ritu.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.Model.BlogCategory
import com.srzone.ritu.R

class CategoryBlogsAdapter(
    private val blogCategories: MutableList<BlogCategory?>,
    private val activity: Activity?
) : RecyclerView.Adapter<CategoryBlogsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.category_blog_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val category = blogCategories[i] ?: return

        // Fix 1: Use the name from JSON so it's impossible to mismatch the title
        viewHolder.categoryNameTv.text = category.name

        val blogList = category.blogList
        if (blogList != null && activity != null) {
            // REMOVED: blogList.shuffle() -> Do this in the Fragment instead!

            viewHolder.categoryBlogsRecyclerView.layoutManager =
                LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            val cleanList = blogList.filterNotNull().toMutableList()

            // Fix 2: Optimization - Only set adapter if it's not already set
            // Or better yet, use a ListAdapter/DiffUtil for smoother performance.
            viewHolder.categoryBlogsRecyclerView.adapter =
                CategoryFeaturedBlogAdapter(cleanList, activity)
        }
    }

    override fun getItemCount(): Int {
        return blogCategories.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryBlogsRecyclerView: RecyclerView = view.findViewById(R.id.categoryBlogsRecycler)
        val categoryNameTv: TextView = view.findViewById(R.id.categoryNameTv)
    }
}
