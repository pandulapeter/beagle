package com.pandulapeter.beagle

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.WindowInsets
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.dimension
import com.pandulapeter.beagle.core.util.extension.drawable

class DebugMenuView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RecyclerView(BeagleCore.implementation.getThemedContext(context), attrs, defStyleAttr) {

    //TODO: in the ui-view module call notify the listeners on attach / detach
    init {
        BeagleCore.implementation.setupRecyclerView(this)
        setBackgroundFromWindowBackground()
        minimumWidth = context.dimension(R.dimen.beagle_minimum_size)
        minimumHeight = context.dimension(R.dimen.beagle_minimum_size)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            setOnApplyWindowInsetsListener { _, insets -> onApplyWindowInsets(insets) }
            requestApplyInsets()
        }
    }

    //TODO: Does not work!
    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            insets?.let {
                setPadding(it.systemWindowInsetLeft, it.systemWindowInsetTop, it.systemWindowInsetRight, it.systemWindowInsetBottom)
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