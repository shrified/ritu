package com.srzone.ritu.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.R
import com.srzone.ritu.ThemesFiles.MyCustomTheme
import com.google.android.material.card.MaterialCardView

abstract class CustomThemesSelectorAdapter(
    private val context: Activity,
    private var customThemes: ArrayList<MyCustomTheme?>,
    private var checkedPosition: Int
) : RecyclerView.Adapter<CustomThemesSelectorAdapter.ViewHolder>() {
    private var lastPosition = -1

    abstract fun onThemeItemSelected(myCustomTheme: MyCustomTheme?)

    fun setThemes(arrayList: ArrayList<MyCustomTheme?>) {
        this.customThemes = arrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.theme_selector_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        this.customThemes[i]?.let { viewHolder.bind(it) }
        setAnimation(viewHolder.itemView, i)
    }

    override fun getItemCount(): Int {
        return this.customThemes.size
    }

    private fun setAnimation(view: View, i: Int) {
        if (i > this.lastPosition) {
            view.startAnimation(
                AnimationUtils.loadAnimation(
                    this.context,
                    R.anim.from_right_slider_animation
                )
            )
            this.lastPosition = i
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: MaterialCardView = view.findViewById(R.id.cardView)
        val introImg: ImageView = view.findViewById(R.id.introImg)

        fun bind(myCustomTheme: MyCustomTheme) {
            if (this@CustomThemesSelectorAdapter.checkedPosition != -1) {
                if (this@CustomThemesSelectorAdapter.checkedPosition == bindingAdapterPosition) {
                    this.cardView.strokeWidth = 5
                    this.cardView.strokeColor = ContextCompat.getColor(
                        this@CustomThemesSelectorAdapter.context,
                        myCustomTheme.themeColor
                    )
                } else {
                    this.cardView.strokeWidth = 0
                }
            } else {
                this.cardView.strokeWidth = 0
            }
            this.introImg.setImageResource(myCustomTheme.bgImg)
            this.itemView.setOnClickListener {
                handleItemClick(myCustomTheme)
            }
        }


        private fun handleItemClick(myCustomTheme: MyCustomTheme?) {
            val position = bindingAdapterPosition
            if (position == RecyclerView.NO_POSITION) return
            
            if (this@CustomThemesSelectorAdapter.checkedPosition != position) {
                val oldPosition = this@CustomThemesSelectorAdapter.checkedPosition
                this@CustomThemesSelectorAdapter.checkedPosition = position
                if (oldPosition != -1) {
                    this@CustomThemesSelectorAdapter.notifyItemChanged(oldPosition)
                }
                this@CustomThemesSelectorAdapter.notifyItemChanged(position)
            }
            this@CustomThemesSelectorAdapter.onThemeItemSelected(myCustomTheme)
        }
    }

    val selectedTheme: MyCustomTheme?
        get() {
            val i = this.checkedPosition
            if (i != -1 && i < customThemes.size) {
                return this.customThemes[i]
            }
            return null
        }
}
