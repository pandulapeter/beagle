package com.pandulapeter.debugMenu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

internal class DebugMenuDrawerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    oldViews: List<View> = emptyList(),
    drawer: View? = null
) : DrawerLayout(context, attrs, defStyleAttr) {

    init {
        addView(FrameLayout(context).apply { oldViews.forEach { view -> addView(view) } }, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(drawer, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, GravityCompat.END))
    }
}