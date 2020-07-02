package com.pandulapeter.beagle

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.dimension
import com.pandulapeter.beagle.core.util.extension.drawable

//TODO: in the ui-view module should notify the listeners when this view is attached / detached
class DebugMenuView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context.applyTheme(), attrs, defStyleAttr) {

    private val verticalMargin = context.dimension(R.dimen.beagle_item_vertical_margin)
    private val recyclerView = RecyclerView(context.applyTheme(), attrs, defStyleAttr).apply {
        overScrollMode = View.OVER_SCROLL_NEVER
        clipToPadding = false
        setPadding(0, verticalMargin, 0, verticalMargin)
        BeagleCore.implementation.setupRecyclerView(this)
        minimumWidth = context.dimension(R.dimen.beagle_minimum_size)
        minimumHeight = context.dimension(R.dimen.beagle_minimum_size)
    }

    init {
        setBackgroundFromWindowBackground()
        addView(recyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            setOnApplyWindowInsetsListener { _, insets -> onApplyWindowInsets(insets) }
            requestApplyInsets()
        }
    }

    //TODO: Needs to be improved
    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            insets?.let {
                recyclerView.setPadding(it.systemWindowInsetLeft, it.systemWindowInsetTop + verticalMargin, it.systemWindowInsetRight, it.systemWindowInsetBottom + verticalMargin)
            }
        }
        return super.onApplyWindowInsets(insets)
    }

    private fun setBackgroundFromWindowBackground() {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            setBackgroundColor(typedValue.data)
        } else {
            background = context.drawable(typedValue.resourceId)
        }
    }
}