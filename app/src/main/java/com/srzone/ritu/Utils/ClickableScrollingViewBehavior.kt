package com.srzone.ritu.Utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class ClickableScrollingViewBehavior(context: Context, attributeSet: AttributeSet?) :
    AppBarLayout.ScrollingViewBehavior(context, attributeSet) {
    
    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: View,
        ev: MotionEvent
    ): Boolean {
        return false
    }
}
