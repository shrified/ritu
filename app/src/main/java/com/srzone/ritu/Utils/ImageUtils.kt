package com.srzone.ritu.Utils

import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.core.content.ContextCompat

object ImageUtils {
    fun setTint(imageView: ImageView, i: Int) {
        imageView.setColorFilter(
            ContextCompat.getColor(imageView.getContext(), i),
            PorterDuff.Mode.SRC_IN
        )
    }
}
