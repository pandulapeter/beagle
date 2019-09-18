package com.pandulapeter.debugMenu.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Dimension
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.utils.dimension

internal class DebugMenuDrawerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    oldViews: List<View> = emptyList(),
    drawer: View? = null,
    @Dimension drawerWidth: Int? = null
) : DrawerLayout(context, attrs, defStyleAttr) {

    init {
        addView(FrameLayout(context).apply { oldViews.forEach { view -> addView(view) } }, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(drawer, LayoutParams(drawerWidth ?: context.dimension(R.dimen.drawer_width), LayoutParams.MATCH_PARENT, GravityCompat.END))
    }
}